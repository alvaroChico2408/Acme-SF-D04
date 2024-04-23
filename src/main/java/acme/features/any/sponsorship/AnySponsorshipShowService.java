
package acme.features.any.sponsorship;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Any;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.projects.Project;
import acme.entities.sponsorship.Sponsorship;
import acme.entities.sponsorship.Type;

@Service
public class AnySponsorshipShowService extends AbstractService<Any, Sponsorship> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnySponsorshipRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int sponsorshipId;
		Sponsorship sponsorship;

		sponsorshipId = super.getRequest().getData("id", int.class);
		sponsorship = this.repository.findSponsorshipById(sponsorshipId).orElse(null);
		status = sponsorship != null && sponsorship.isPublished();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Sponsorship object;
		int sponsorshipId;

		sponsorshipId = super.getRequest().getData("id", int.class);
		object = this.repository.findSponsorshipById(sponsorshipId).orElse(null);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final Sponsorship object) {
		assert object != null;

		Dataset dataset;

		final SelectChoices choices = new SelectChoices();
		Collection<Project> projects;
		projects = this.repository.findPublishedProjects();
		SelectChoices types = SelectChoices.from(Type.class, object.getType());

		for (final Project c : projects)
			if (object.getProject() != null && object.getProject().getId() == c.getId())
				choices.add(Integer.toString(c.getId()), "Code: " + c.getCode() + " - " + "Title: " + c.getTitle(), true);
			else
				choices.add(Integer.toString(c.getId()), "Code: " + c.getCode() + " - " + "Title: " + c.getTitle(), false);

		dataset = super.unbind(object, "code", "moment", "durationInitial", "durationFinal", "amount", "type", "email", "link", "published", "project");
		dataset.put("sponsorUsername", object.getSponsor().getUserAccount().getUsername());
		dataset.put("project", choices.getSelected().getKey());
		dataset.put("projects", choices);
		dataset.put("types", types);
		super.getResponse().addData(dataset);
	}

}
