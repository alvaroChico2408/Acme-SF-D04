
package acme.features.authenticated.notice;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Authenticated;
import acme.client.data.accounts.UserAccount;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.notice.Notice;
import acme.entities.systemConfiguration.SystemConfiguration;
import spam.SpamFilter;

@Service
public class AuthenticatedNoticeCreateService extends AbstractService<Authenticated, Notice> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedNoticeRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Notice object;
		int id = super.getRequest().getPrincipal().getAccountId();
		UserAccount userAccount = this.repository.findUserAccountById(id);
		String formato = String.format("< %s > - < %s , %s > ", userAccount.getUsername(), userAccount.getIdentity().getSurname(), userAccount.getIdentity().getName());

		object = new Notice();
		object.setInstantiationMoment(MomentHelper.getCurrentMoment());
		object.setAuthor(formato);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Notice object) {
		assert object != null;

		super.bind(object, "title", "message", "email", "link");
	}

	@Override
	public void validate(final Notice object) {
		assert object != null;

		if (!this.getBuffer().getErrors().hasErrors("title") && object.getTitle() != null) {
			SystemConfiguration sc = this.repository.findSystemConfiguration();
			SpamFilter spam = new SpamFilter(sc.getSpamWords(), sc.getSpamThreshold());

			super.state(!spam.isSpam(object.getTitle()), "title", "authenticated.Notice.form.error.spam");
		}

		if (!this.getBuffer().getErrors().hasErrors("message") && object.getTitle() != null) {
			SystemConfiguration sc = this.repository.findSystemConfiguration();
			SpamFilter spam = new SpamFilter(sc.getSpamWords(), sc.getSpamThreshold());

			super.state(!spam.isSpam(object.getMessage()), "message", "authenticated.Notice.form.error.spam");
		}

		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "javax.validation.constraints.AssertTrue.message");
	}

	@Override
	public void perform(final Notice object) {
		assert object != null;

		Date instantiationMoment;

		instantiationMoment = MomentHelper.getCurrentMoment();
		object.setInstantiationMoment(instantiationMoment);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Notice object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "instantiationMoment", "title", "author", "message", "email", "link");
		dataset.put("confirmation", false);
		dataset.put("readonly", false);

		super.getResponse().addData(dataset);
	}

}
