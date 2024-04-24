
package acme.features.client.contracts;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.datatypes.Money;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.components.AuxiliarService;
import acme.entities.contract.Contract;
import acme.roles.Client;

@Service
public class ClientContractUpdateService extends AbstractService<Client, Contract> {

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
		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Contract existing;
			existing = this.repository.findContractByCode(object.getCode());
			final Contract contract2 = object.getCode().equals("") || object.getCode() == null ? null : this.repository.findContractById(object.getId());
			super.state(existing == null || contract2.equals(existing), "code", "client.contract.form.error.code");
		}

		final Collection<Contract> contracts = this.repository.findContractsFromProject(object.getProject().getId());
		contracts.remove(object);
		contracts.add(object);

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

		dataset.put("projectTitle", object.getProject().getCode());
		super.getResponse().addData(dataset);
	}
}
