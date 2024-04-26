
package acme.features.authenticated.sponsor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Authenticated;
import acme.client.data.accounts.Principal;
import acme.client.data.accounts.UserAccount;
import acme.client.data.models.Dataset;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractService;
import acme.entities.systemConfiguration.SystemConfiguration;
import acme.roles.Sponsor;
import spam.SpamFilter;

@Service
public class AuthenticatedSponsorCreateService extends AbstractService<Authenticated, Sponsor> {
	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedSponsorRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		status = !super.getRequest().getPrincipal().hasRole(Sponsor.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Sponsor object;
		Principal principal;
		int userAccountId;
		UserAccount userAccount;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		userAccount = this.repository.findUserAccountById(userAccountId);

		object = new Sponsor();
		object.setUserAccount(userAccount);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Sponsor object) {
		assert object != null;

		super.bind(object, "name", "benefits", "web", "email");
	}

	@Override
	public void validate(final Sponsor object) {
		assert object != null;

		if (!this.getBuffer().getErrors().hasErrors("name") && object.getName() != null) {
			SystemConfiguration sc = this.repository.findSystemConfiguration();
			SpamFilter spam = new SpamFilter(sc.getSpamWords(), sc.getSpamThreshold());

			super.state(!spam.isSpam(object.getName()), "name", "authenticated.sponsor.form.error.spam");
		}

		if (!this.getBuffer().getErrors().hasErrors("benefits") && object.getBenefits() != null) {
			SystemConfiguration sc = this.repository.findSystemConfiguration();
			SpamFilter spam = new SpamFilter(sc.getSpamWords(), sc.getSpamThreshold());

			super.state(!spam.isSpam(object.getBenefits()), "benefits", "authenticated.sponsor.form.error.spam");
		}
	}

	@Override
	public void perform(final Sponsor object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final Sponsor object) {
		assert object != null;

		final Dataset dataset;

		dataset = super.unbind(object, "name", "benefits", "web", "email");

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}
}
