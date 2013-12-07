package agui;

import java.io.UnsupportedEncodingException;

import auth.*;
import auth.Resolver.UserNotFoundException;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;

public class Alogin {

	protected Shell Alogin;
	private boolean login_status = false;
	private Text username_text;
	private Text password_text;
	private AuthClient portclient;
	private Button login_button;

	/**
	 * Open the window.
	 */
	public Alogin(AuthClient tempclient) {
		portclient = tempclient;
		new Sanitizer();
	}

	public void open() {
		Display display = Display.getDefault();
		createContents();
		Alogin.open();
		Alogin.layout();
		while (!Alogin.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 * 
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() {
		Alogin = new Shell(SWT.CLOSE | SWT.TITLE | SWT.PRIMARY_MODAL);
		Alogin.setSize(355, 183);
		Alogin.setText("Portunes | Administer Login");
		Alogin.setLayout(null);

		Group grpEnterAdminInformation = new Group(Alogin, SWT.NONE);
		grpEnterAdminInformation.setBounds(10, 10, 327, 135);

		Label lblAdministrator = new Label(grpEnterAdminInformation, SWT.NONE);
		lblAdministrator.setBounds(10, 27, 73, 15);
		lblAdministrator.setText("Administrator:");

		Label lblPassword = new Label(grpEnterAdminInformation, SWT.NONE);
		lblPassword.setBounds(10, 58, 55, 15);
		lblPassword.setText("Password:");

		username_text = new Text(grpEnterAdminInformation, SWT.BORDER);
		username_text.setBounds(89, 24, 224, 21);

		password_text = new Text(grpEnterAdminInformation, SWT.BORDER);
		password_text.setBounds(71, 58, 241, 22);

		login_button = new Button(grpEnterAdminInformation, SWT.NONE);
		login_button.addSelectionListener(new LoginAction());
		login_button.setBounds(194, 86, 123, 37);
		login_button.setText("Login");

	}

	public class LoginAction extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent e) {
			if (e.getSource() == login_button) {
				String username = username_text.getText();
				String password = password_text.getText();
				if (Sanitizer.isCleanInput(username)
						&& Sanitizer.isCleanInput(password)) {
					Request loginattempt = null;
					try {
						loginattempt = new CHECK(username,
								password.getBytes("UTF-8"));
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						loginattempt = portclient.exchange(loginattempt);
					} catch (UserNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					MessageBox messageBox = new MessageBox(Alogin,
							SWT.ICON_ERROR);
					if (username_text.getText() == ""
							|| password_text.getText() == "")
						messageBox.setMessage("Empty Field(s)");
					else
						messageBox.setMessage("Invalid Character in Field(s)");
					messageBox.open();
				}
			}
		}

	}
}
