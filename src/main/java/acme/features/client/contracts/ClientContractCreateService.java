
package acme.features.client.contracts;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.datatypes.Money;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.components.AuxiliarService;
import acme.entities.contract.Contract;
import acme.entities.projects.Project;
import acme.entities.systemConfiguration.SystemConfiguration;
import acme.roles.Client;
import spam.SpamFilter;

@Service
public class ClientContractCreateService extends AbstractService<Client, Contract> {

	@Autowired
	protected ClientContractRepository	repository;

	@Autowired
	protected AuxiliarService			auxiliarService;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Contract object;
		object = new Contract();
		final Client client = this.repository.findOneClientById(super.getRequest().getPrincipal().getActiveRoleId());
		object.setClient(client);
		object.setPublished(false);
		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Contract object) {
		if (object == null)
			throw new IllegalArgumentException("No object found");
		super.bind(object, "code", "providerName", "instantiationMoment", "customerName", "goals", "budget", "project");
	}

	@Override
	public void validate(final Contract object) {
		if (object == null)
			throw new IllegalArgumentException("No object found");

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Contract existing;
			existing = this.repository.findContractByCode(object.getCode());
			super.state(existing == null, "code", "client.contract.form.error.code");
		}

		if (!super.getBuffer().getErrors().hasErrors("instantiationMoment")) {
			Date minDate = new Date(946681200000L); // 2000/01/01 00:00:00
			super.state(MomentHelper.isAfterOrEqual(object.getInstantiationMoment(), minDate), "instantiationMoment", "client.contract.form.error.instantiationMoment");
		}

		final Collection<Contract> contracts = this.repository.findContractsFromProject(object.getProject().getId());

		Money ratioEuros;
		ratioEuros = new Money();
		ratioEuros.setAmount(100.00);
		ratioEuros.setCurrency("EUR");

		if (!contracts.isEmpty()) {

			boolean overBudget;
			double totalBudget = 0.0;
			for (Contract c : contracts)
				totalBudget = totalBudget + this.auxiliarService.changeCurrency(c.getBudget()).getAmount();
			if (totalBudget > object.getProject().getCost() * this.auxiliarService.changeCurrency(ratioEuros).getAmount())
				overBudget = false;
			else
				overBudget = true;
			super.state(overBudget, "*", "client.contract.form.error.overBudget");
		}

		if (!super.getBuffer().getErrors().hasErrors("budget")) {

			Money maxEuros;

			maxEuros = new Money();
			maxEuros.setAmount(1000000.00);
			maxEuros.setCurrency("EUR");
			super.state(this.auxiliarService.validatePrice(object.getBudget(), 0.00, maxEuros.getAmount()), "budget", "client.contract.form.error.budget");
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
		this.repository.save(object);
	}

	@Override
	public void unbind(final Contract object) {
		if (object == null)
			throw new IllegalArgumentException("No object found");
		Dataset dataset;
		dataset = super.unbind(object, "code", "instantiationMoment", "providerName", "customerName", "goals", "budget", "project", "client", "published");

		final SelectChoices choices = new SelectChoices();
		Collection<Project> projects;
		projects = this.repository.findPublishedProjects();
		for (final Project p : projects)
			choices.add(Integer.toString(p.getId()), p.getCode() + " - " + p.getTitle(), false);

		dataset.put("projects", choices);
		super.getResponse().addData(dataset);
	}
}
