
package acme.features.any.contract;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Any;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.components.AuxiliarService;
import acme.entities.contract.Contract;
import acme.entities.contract.ProgressLog;

@Service
public class AnyContractShowService extends AbstractService<Any, Contract> {

	@Autowired
	protected AnyContractRepository	repository;

	@Autowired
	protected AuxiliarService		auxiliarService;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		Contract object;
		int id;
		id = super.getRequest().getData("id", int.class);
		object = this.repository.findContractById(id);
		super.getResponse().setAuthorised(object.isPublished());
	}

	@Override
	public void load() {
		Contract object;
		int id;
		id = super.getRequest().getData("id", int.class);
		object = this.repository.findContractById(id);
		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final Contract object) {
		assert object != null;
		Dataset dataset;
		dataset = super.unbind(object, "code", "providerName", "customerName", "goals", "budget", "published");
		final List<ProgressLog> progressLogs = (List<ProgressLog>) this.repository.findProgressLogsByContract(object.getId());
		dataset.put("projectTitle", object.getProject().getCode());
		dataset.put("hasProgressLogs", !progressLogs.isEmpty());
		super.getResponse().addData(dataset);
	}

}
