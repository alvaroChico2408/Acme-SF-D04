
package acme.features.client.progresslog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.contract.Contract;
import acme.entities.contract.ProgressLog;
import acme.roles.Client;

@Service
public class ClientProgressLogListService extends AbstractService<Client, ProgressLog> {

	@Autowired
	protected ClientProgressLogRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		Contract object;
		int masterId;
		masterId = super.getRequest().getData("masterId", int.class);
		object = this.repository.findContractById(masterId);
		final Principal principal = super.getRequest().getPrincipal();
		final int userAccountId = principal.getAccountId();
		super.getResponse().setAuthorised(object.getClient().getUserAccount().getId() == userAccountId);
	}

	@Override
	public void load() {
		Collection<ProgressLog> objects;
		int masterId;
		masterId = super.getRequest().getData("masterId", int.class);
		objects = this.repository.findProgressLogsByContract(masterId);
		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final ProgressLog object) {
		assert object != null;
		Dataset dataset;
		dataset = super.unbind(object, "recordId", "completeness", "responsiblePerson");
		int masterId;
		masterId = super.getRequest().getData("masterId", int.class);
		super.getResponse().addGlobal("masterId", masterId);
		dataset.put("masterId", masterId);
		final Contract c = this.repository.findContractById(masterId);
		final boolean showCreate = !c.isPublished();
		super.getResponse().addGlobal("showCreate", showCreate);
		super.getResponse().addData(dataset);
		super.getResponse().addGlobal("contractCode", c.getCode());
	}

	@Override
	public void unbind(final Collection<ProgressLog> object) {
		assert object != null;
		int masterId;
		masterId = super.getRequest().getData("masterId", int.class);
		super.getResponse().addGlobal("masterId", masterId);
		final Contract c = this.repository.findContractById(masterId);
		final boolean showCreate = !c.isPublished();
		super.getResponse().addGlobal("showCreate", showCreate);

		super.getResponse().addGlobal("contractCode", c.getCode());

	}

}
