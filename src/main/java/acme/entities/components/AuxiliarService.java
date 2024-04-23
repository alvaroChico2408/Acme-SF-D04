
package acme.entities.components;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import acme.client.data.datatypes.Money;
import acme.client.helpers.MomentHelper;
import acme.entities.currencyCache.CurrencyCache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service
public class AuxiliarService {

	@Autowired
	private AuxiliarRepository repository;


	public boolean validatePrice(final Money price, final Double minAm, final Double maxAm) {
		return price.getAmount() >= minAm && price.getAmount() < maxAm;
	}

	public boolean validateCurrency(final Money price) {
		final String aceptedCurrencies = this.repository.findSystemConfiguration().getAcceptedCurrencies();
		final List<String> currencies = Arrays.asList(aceptedCurrencies.split(","));
		return currencies.contains(price.getCurrency());
	}

	public String translateMoney(final Money money, final String lang) {
		String res;
		res = "";
		if (lang.equals("en")) {
			final double parteDecimal = money.getAmount() % 1;
			final double parteEntera = money.getAmount() - parteDecimal;
			res = parteEntera + "." + parteDecimal + " " + money.getCurrency();
		} else if (lang.equals("es"))
			res = money.getAmount() + " " + money.getCurrency();
		return res;
	}

	public Money changeCurrency(final Money money) {
		Money res;
		final String currentCurrency = this.repository.findSystemConfiguration().getSystemCurrency();
		res = new Money();
		if (money != null && !money.getCurrency().equals(currentCurrency)) {
			CurrencyCache cc;
			cc = this.repository.getCurrencyCacheByChange(money.getCurrency(), currentCurrency);
			if (cc == null || MomentHelper.isAfter(MomentHelper.getCurrentMoment(), MomentHelper.deltaFromMoment(cc.getDate(), 1, ChronoUnit.DAYS))) {
				final String apiBase = "https://api.freecurrencyapi.com/v1/latest?apikey=";
				final String apikey1 = "fca_live_37uUsghg9mD8ND0pbyBnXMovJaQowKF1TyPcjJ4B";
				final String requestURL = apiBase + apikey1 + "&currencies=" + money.getCurrency() + "&base_currency=" + currentCurrency;
				final OkHttpClient client = new OkHttpClient();
				final Request request = new Request.Builder().url(requestURL).build();
				Response response;
				try {
					response = client.newCall(request).execute();
					final String responseBody = response.body().string();
					final ObjectMapper mapper = new ObjectMapper();
					final JsonNode jsonNode = mapper.readTree(responseBody);
					final Double value = jsonNode.get("data").get(money.getCurrency()).asDouble();
					if (value != null) {
						res.setCurrency(currentCurrency);
						res.setAmount(value * money.getAmount());
					}
					if (cc == null) {
						cc = new CurrencyCache();
						cc.setDate(MomentHelper.getCurrentMoment());
						cc.setDestinationCurrency(currentCurrency);
						cc.setOrigenCurrency(money.getCurrency());
						cc.setRatio(value);
						this.repository.save(cc);
					} else {
						cc.setDate(MomentHelper.getCurrentMoment());
						cc.setRatio(value);
						this.repository.save(cc);
					}
				} catch (final Exception e) {
					res = money;
				}
			} else {
				res.setCurrency(currentCurrency);
				res.setAmount(cc.getRatio() * money.getAmount());
			}
		} else
			res = money;
		return res;
	}

}
