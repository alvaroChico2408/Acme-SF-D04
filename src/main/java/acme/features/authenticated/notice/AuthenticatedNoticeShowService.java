
package acme.features.authenticated.notice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Authenticated;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.notice.Notice;

@Service
public class AuthenticatedNoticeShowService extends AbstractService<Authenticated, Notice> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedNoticeRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int noticeId;
		Notice notice;

		noticeId = super.getRequest().getData("id", int.class);
		notice = this.repository.findNoticeById(noticeId);
		status = notice != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Notice object;
		int noticeId;

		noticeId = super.getRequest().getData("id", int.class);
		object = this.repository.findNoticeById(noticeId);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final Notice object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "instantiationMoment", "title", "author", "message", "email", "link");

		super.getResponse().addData(dataset);
	}

}
