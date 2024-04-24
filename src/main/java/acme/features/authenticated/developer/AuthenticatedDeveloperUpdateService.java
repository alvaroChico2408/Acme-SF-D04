
package acme.features.authenticated.developer;

import org.springframework.stereotype.Service;

import acme.client.data.accounts.Authenticated;
import acme.client.services.AbstractService;
import acme.roles.Developer;

@Service
public class AuthenticatedDeveloperUpdateService extends AbstractService<Authenticated, Developer> {

}
