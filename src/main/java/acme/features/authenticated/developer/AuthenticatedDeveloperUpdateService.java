
package acme.features.authenticated.developer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Authenticated;
import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractService;
import acme.entities.systemConfiguration.SystemConfiguration;
import acme.roles.Developer;
import spam.SpamFilter;

@Service
public class AuthenticatedDeveloperUpdateService extends AbstractService<Authenticated, Developer> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedDeveloperRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		status = super.getRequest().getPrincipal().hasRole(Developer.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Developer object;
		Principal principal;
		int userAccountId;
		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		object = this.repository.findDeveloperByUserAccountId(userAccountId);
		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Developer object) {
		assert object != null;

		super.bind(object, "degree", "specialisation", "skills", "email", "link");
	}

	@Override
	public void validate(final Developer object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("degree")) {
			SystemConfiguration sc = this.repository.findSystemConfiguration();
			SpamFilter spam = new SpamFilter(sc.getSpamWords(), sc.getSpamThreshold());

			super.state(!spam.isSpam(object.getDegree()), "degree", "authenticated.developer.form.error.spam");
		}

		if (!super.getBuffer().getErrors().hasErrors("specialisation")) {
			SystemConfiguration sc = this.repository.findSystemConfiguration();
			SpamFilter spam = new SpamFilter(sc.getSpamWords(), sc.getSpamThreshold());

			super.state(!spam.isSpam(object.getSpecialisation()), "specialisation", "authenticated.developer.form.error.spam");
		}

		if (!super.getBuffer().getErrors().hasErrors("skills")) {
			SystemConfiguration sc = this.repository.findSystemConfiguration();
			SpamFilter spam = new SpamFilter(sc.getSpamWords(), sc.getSpamThreshold());

			super.state(!spam.isSpam(object.getSkills()), "skills", "authenticated.developer.form.error.spam");
		}
	}

	@Override
	public void perform(final Developer object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final Developer object) {
		assert object != null;
		Dataset dataset;
		dataset = super.unbind(object, "degree", "specialisation", "skills", "email", "link");
		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
