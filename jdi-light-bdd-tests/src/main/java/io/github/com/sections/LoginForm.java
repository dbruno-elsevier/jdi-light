package io.github.com.sections;

import com.epam.jdi.light.elements.composite.Form;
import com.epam.jdi.light.ui.html.elements.common.Button;
import com.epam.jdi.light.ui.html.elements.common.TextField;
import io.github.com.entities.User;

import static io.github.com.pages.Header.userIcon;

public class LoginForm extends Form<User> {
	public TextField name;
	public TextField password;
	public Button loginButton;

	public void shouldBeOpened() {
		if (isHidden()) {
			userIcon.click();
		}
	}

	public boolean isHidden() {
		return name.isHidden();
	}
}