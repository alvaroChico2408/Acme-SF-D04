
package acme.features.client.contracts;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.datatypes.Money;
import acme.client.data.models.Dataset;
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
		contracts.remove(object);

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Contract existing;
			existing = this.repository.findContractByCode(object.getCode());
			final Contract contract2 = object.getCode().equals("") || object.getCode() == null ? null : this.repository.findContractById(object.getId());
			super.state(existing == null || contract2.equals(existing), "code", "client.contract.form.error.code");
		}

		Collection<ProgressLog> progressLog = this.repository.findProgressLogsByContract(object.getId());
		super.state(!progressLog.isEmpty(), "*", "client.contract.form.error.noprogresslog");

		if (!progressLog.isEmpty()) {
			int numProgressLogsPublish = progressLog.stream().filter(ProgressLog::isPublished).toList().size();
			boolean allProgressLogsPublish = progressLog.size() == numProgressLogsPublish;
			super.state(allProgressLogsPublish, "*", "client.contract.form.error.progresslognotpublished");
		}

		double totalAmount = 0;

		double converterHourToEUR = 100;

		if (!super.getBuffer().getErrors().hasErrors("budget")) {

			Money maxEuros;

			maxEuros = new Money();
			maxEuros.setAmount(1000000.01);
			maxEuros.setCurrency("EUR");
			super.state(this.auxiliarService.validatePrice(object.getBudget(), 0.00, maxEuros.getAmount()), "budget", "client.contract.form.error.budget");
			if (object.getProject() != null) {
				Collection<Contract> listAllContracts = this.repository.findContractsFromProject(object.getProject().getId());
				listAllContracts.remove(object);
				listAllContracts.add(object);
				totalAmount = listAllContracts.stream().map(x -> x.getBudget().getAmount()).collect(Collectors.summingDouble(x -> x));

			}
			super.state(this.auxiliarService.validateCurrency(object.getBudget()), "budget", "client.contract.form.error.budget2");
			double totalCost = object.getProject() != null ? object.getProject().getCost() * converterHourToEUR : 0;
			super.state(totalAmount <= totalCost, "budget", "client.contract.form.error.overBudget");
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
