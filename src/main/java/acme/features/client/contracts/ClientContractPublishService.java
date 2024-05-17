
package acme.features.client.contracts;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.datatypes.Money;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.components.AuxiliarService;
import acme.entities.contract.Contract;
import acme.entities.contract.ProgressLog;
import acme.entities.systemConfiguration.SystemConfiguration;
import acme.roles.Client;
import spam.SpamFilter;

@Service
public class ClientContractPublishService extends AbstractService<Client, Contract> {

	@Autowired
	protected ClientContractRepository	repository;

	@Autowired
	protected AuxiliarService			auxiliarService;

	// AbstractService interface -------------------------------------


	@Override
	public void authorise() {
		Contract object;
		int id;
		id = super.getRequest().getData("id", int.class);
		object = this.repository.findContractById(id);
		final Principal principal = super.getRequest().getPrincipal();
		final int userAccountId = principal.getAccountId();
		super.getResponse().setAuthorised(object.getClient().getUserAccount().getId() == userAccountId && !object.isPublished());
	}

	@Override
	public void load() {
		Contract object;
		object = new Contract();
		object.setPublished(false);
		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Contract object) {
		if (object == null)
			throw new IllegalArgumentException("No object found");
		Contract object2;
		int id;

		id = super.getRequest().getData("id", int.class);
		object2 = this.repository.findContractById(id);
		object.setProject(object2.getProject());
		object.setClient(object2.getClient());
		super.bind(object, "code", "providerName", "instantiationMoment", "customerName", "goals", "budget");
	}

	@Override
	public void validate(final Contract object) {
		if (object == null)
			throw new IllegalArgumentException("No object found");
		final Collection<Contract> contracts = this.repository.findContractsFromProject(object.getProject().getId());

		if (!super.getBuffer().getErrors().hasErrors("instantiationMoment")) {
			Date minDate = new Date(946681200000L); // 2000/01/01 00:00
			super.state(MomentHelper.isAfterOrEqual(object.getInstantiationMoment(), minDate), "instantiationMoment", "client.contract.form.error.instantiationMoment");
		}

		Money ratioEuros;
		ratioEuros = new Money();
		ratioEuros.setAmount(100.00);
		ratioEuros.setCurrency("EUR");

		if (!contracts.isEmpty()) {

			boolean overBudget;
			double totalBudget = 0.0;
			for (Contract c : contracts)
				totalBudget = totalBudget + this.auxiliarService.changeCurrency(c.getBudget()).getAmount();
			if (totalBudget < object.getProject().getCost() * this.auxiliarService.changeCurrency(ratioEuros).getAmount())
				overBudget = false;
			else
				overBudget = true;
			super.state(overBudget, "*", "manager.project.form.error.overBudget");
		}

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Contract existing;
			existing = this.repository.findContractByCode(object.getCode());
			final Contract contract2 = object.getCode().equals("") || object.getCode() == null ? null : this.repository.findContractById(object.getId());
			super.state(existing == null || contract2.equals(existing), "code", "client.contract.form.error.code");
		}

		Collection<ProgressLog> progressLog = this.repository.findProgressLogsByContract(object.getId());
		super.state(!progressLog.isEmpty(), "*", "client.contract.form.error.noprogresslog");

		if (!progressLog.isEmpty()) {
			int numUserStoryPublish = progressLog.stream().filter(ProgressLog::isPublished).toList().size();
			boolean allUserStoriesPublish = progressLog.size() == numUserStoryPublish;
			super.state(allUserStoriesPublish, "*", "client.contract.form.error.progresslognotpublished");
		}

		if (!super.getBuffer().getErrors().hasErrors("budget")) {
			Money maxEuros;

			maxEuros = new Money();
			maxEuros.setAmount(1000000.00);
			maxEuros.setCurrency("EUR");
			double maximo = object.getProject().getCost() * this.auxiliarService.changeCurrency(ratioEuros).getAmount();
			super.state(this.auxiliarService.validatePrice(object.getBudget(), 0.00, maximo), "cost", "client.contract.form.error.budget");
			super.state(this.auxiliarService.validateCurrency(object.getBudget()), "budget", "client.contract.form.error.cost2");
		}

		SystemConfiguration sc = this.repository.findSystemConfiguration();
		SpamFilter spam = new SpamFilter(sc.getSpamWords(), sc.getSpamThreshold());

		if (!super.getBuffer().getErrors().hasErrors("providerName"))
			super.state(!spam.isSpam(object.getProviderName()), "providerName", "client.contract.form.error.spam");

		if (!super.getBuffer().getErrors().hasErrors("customerName"))
			super.state(!spam.isSpam(object.getCustomerName()), "customerName", "client.contract.form.error.spam");

		if (!super.getBuffer().getErrors().hasErrors("goals"))
			super.state(!spam.isSpam(object.getGoals()), "goals", "client.contract.form.error.spam");
	}

	@Override
	public void perform(final Contract object) {
		if (object == null)
			throw new IllegalArgumentException("No object found");
		object.setPublished(true);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Contract object) {
		if (object == null)
			throw new IllegalArgumentException("No object found");
		Dataset dataset;
		dataset = super.unbind(object, "code", "instantiationMoment", "providerName", "customerName", "goals", "budget", "project", "client", "published");
		dataset.put("projectTitle", object.getProject().getCode());
		super.getResponse().addData(dataset);
	}
}
