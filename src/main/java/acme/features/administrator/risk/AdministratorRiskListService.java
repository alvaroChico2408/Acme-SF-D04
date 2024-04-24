
package acme.features.administrator.risk;

import org.springframework.stereotype.Service;

import acme.client.data.accounts.Administrator;
import acme.client.services.AbstractService;
import acme.entities.risk.Risk;

@Service
public class AdministratorRiskListService extends AbstractService<Administrator, Risk> {

}
