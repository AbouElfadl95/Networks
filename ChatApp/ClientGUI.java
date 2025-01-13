import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.UnknownHostException;

public class ClientGUI extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	int port = 9090;
	boolean ConnectClicked;
	static Client client;
	static String Display = "";
	static JLabel lblNewLabel_1 = new JLabel("");

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientGUI frame = new ClientGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		while(true){
//			try {
//				String sr = client.serverResponse.readLine();
//				Display += sr + '\n';
//				lblNewLabel_1.setText(Display);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} 
//			
			
		}
	}
	
	public void CreateClientAndRun() throws UnknownHostException, IOException {
		client = new Client(port);
		String sr = client.serverResponse.readLine(); 
		Display+= sr + '\n';
		lblNewLabel_1.setText(Display + '\n');

		
	}

	public ClientGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 675, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JRadioButton rdbtnServer = new JRadioButton("Server 1");
		rdbtnServer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				port = 9090;
			}
		});
		rdbtnServer.setBounds(6, 6, 141, 23);
		contentPane.add(rdbtnServer);
		
		JRadioButton rdbtnServer_1 = new JRadioButton("Server 2");
		rdbtnServer_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				port = 1234;
			}
		});
		
		lblNewLabel_1.setBounds(6, 69, 645, 234);
		contentPane.add(lblNewLabel_1);
		
		rdbtnServer_1.setBounds(159, 6, 94, 23);
		contentPane.add(rdbtnServer_1);
		
		textField = new JTextField();
		textField.setBounds(343, 5, 326, 26);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Username:");
		lblNewLabel.setBounds(265, 8, 78, 20);
		contentPane.add(lblNewLabel);
		
		JButton btnNewButton = new JButton("Connect");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					CreateClientAndRun();
					client.userName = client.join(textField.getText());
					Display+= "welcome,To chat with someone, enter their username followed by a : and then your message." + "\n";
					lblNewLabel_1.setText(Display + '\n');
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNewButton.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		btnNewButton.setBounds(108, 41, 192, 29);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Members Of Server 1");
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Display += client.getMemberList1() + "\n";
					lblNewLabel_1.setText(Display);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNewButton_1.setBounds(6, 368, 174, 29);
		contentPane.add(btnNewButton_1);
		
		JButton btnMembersOfServer = new JButton("Members Of Server 2");
		btnMembersOfServer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Display += client.getMemberList2() + "\n";
					lblNewLabel_1.setText(Display);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnMembersOfServer.setBounds(192, 368, 174, 29);
		contentPane.add(btnMembersOfServer);
		
		JButton btnAllMembers = new JButton("All Members");
		btnAllMembers.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Display += client.getMemberList() + "\n";
					lblNewLabel_1.setText(Display);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnAllMembers.setBounds(430, 368, 174, 29);
		contentPane.add(btnAllMembers);
		
		JButton btnSend = new JButton("Send");
		btnSend.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String clientMessage = textField_1.getText();
				String[] s = clientMessage.split(":");
				try {
					client.chat(client.userName, s[0], 2, s[1]);
				} catch (IOException e1){
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnSend.setBounds(534, 342, 117, 29);
		contentPane.add(btnSend);
		
		textField_1 = new JTextField();
		textField_1.setBounds(6, 342, 516, 26);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		JButton btnQuit = new JButton("Quit");
		btnQuit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					client.dos.writeBytes("BYE"+'\n');
					client.socket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Display+="Disconnected from server." + "\n";
				lblNewLabel_1.setText(Display);
			}
		});
		btnQuit.setBounds(216, 393, 117, 29);
		contentPane.add(btnQuit);
	}
}
