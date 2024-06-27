
package acme.features.client.contracts;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.components.AuxiliarService;
import acme.entities.contract.Contract;
import acme.entities.systemConfiguration.SystemConfiguration;
import acme.roles.Client;
import spam.SpamFilter;

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
		object.setInstantiationMoment(object2.getInstantiationMoment());
		super.bind(object, "code", "providerName", "customerName", "goals", "budget");
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

		dataset.put("projectCode", object.getProject().getCode());
		super.getResponse().addData(dataset);
	}
}
