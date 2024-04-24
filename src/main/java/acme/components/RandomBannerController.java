
package acme.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import acme.entities.banner.Banner;

@ControllerAdvice
public class RandomBannerController {

	@Autowired
	RandomBannerService service;


	@ModelAttribute("banner")
	public Banner getBanner() {
		Banner res;
		res = this.service.getRandomBanner();
		return res;
	}
}
