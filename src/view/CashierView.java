package view;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import domain.Order;
import domain.Invoice;
import domain.InvoiceObservable;
import domain.InvoiceObserver;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
/**
 * @author Ruben Schroyen
 */
public class CashierView extends JFrame implements InvoiceObserver {

		/**
		 * @author Ruben Schroyen
		 */
	
		private InvoiceObservable observable ;
		
		private static final long serialVersionUID = 1L;
		private JTextField productId  = new JTextField(5);
		private JLabel productLabel = new JLabel("Product: ");
		private JTextField quantity = new JTextField(3);
		
		public DefaultTableModel getModel() {
			return model;
		}

		public void setModel(DefaultTableModel model) {
			this.model = model;
		}
		
		private JLabel quantityLabel = new JLabel("Quantity: ");
		private JTextField price = new JTextField(5);
		private JLabel pay = new JLabel("To pay: ");
		private JButton addButton = new JButton("Add");
		private JLabel promo = new JLabel("Promotion code: ");
		private JTextField promocode = new JTextField(20);
		private JButton promoButton = new JButton("Enter");
		private JButton confirm = new JButton("Go to payment screen");
		
		
		private DefaultTableModel model = new DefaultTableModel();
		private JTable table;
		
		
		JScrollPane scrollPane = new JScrollPane(table);
	    
	    
		public CashierView(){
			buildTable();
		}
		
		public void buildTable()
		{
			JPanel panel = new JPanel();
			model.addColumn("Description");
			model.addColumn("Quantity");
			model.addColumn("Unit price");
			model.addColumn("Total price");
			table = new JTable(model);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setSize(900, 500);
			price.setEditable(false);
			quantity.setText("1");
			panel.add(new JScrollPane(table));
			panel.add(productLabel);
			panel.add(productId);
			panel.add(quantityLabel);
			panel.add(quantity);
			panel.add(addButton);
			panel.add(pay);
			panel.add(price);
			panel.add(promo);
			panel.add(promocode);
			panel.add(promoButton);
			panel.add(confirm);
			confirm.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					final JFrame frame = new JFrame("Confirm Payment");
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					JPanel panel2 = new JPanel();
					panel2.add(new JLabel("Amount paid by customer:"));
					panel2.add(new JTextField(20));
					JButton cancel = new JButton("Cancel");
					JButton confirm = new JButton("Confirm");
					cancel.addActionListener(new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent e) {
							frame.setVisible(false);
						}
					});
					
					confirm.addActionListener(new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent e) {
							frame.setVisible(false);
						}
					});
					panel2.add(cancel);
					panel2.add(confirm);
					frame.add(panel2);
					frame.setVisible(true);
					frame.pack();
					
				}
			});
			
			this.add(panel);
			table.setVisible(true);
			
		}
		
		
		public String getId(){
			return productId.getText();
		}
		public int getQuantity(){
		    return Integer.parseInt(quantity.getText());
		}
		public double getPrice(){
			return Double.parseDouble(price.getText());
		}

		public void setPrice(double dble ){
			price.setText(Double.toString(dble));
		}
		
		public String getPromocode(){
			return promocode.getText();
		}
		
		public void setPromocode(String code){
			promocode.setText(code);
		}
		
		public void addPriceListener(ActionListener listenForAddButton){
			addButton.addActionListener(listenForAddButton);
		}
		
		public void addPromoListener(ActionListener listenForPromoButton){
			promoButton.addActionListener(listenForPromoButton);
		}
		public void displayErrorMessage(String errorMessage){
			JOptionPane.showMessageDialog(null, errorMessage);
		}
		public void addRow(Order order){
			int count = model.getRowCount();
			String title = order.getProduct().getTitle();
			String quantity = order.getQuantity()+"";
			String price = order.getProduct().getPrice()+"";
			String totalPrice = order.getPrice()+"";
			model.insertRow(count,new Object[]{title,quantity,price,totalPrice});
		}
		@Override
		public void update() {
			Invoice sale= (Invoice) this.observable.getUpdate(this);
			model.setRowCount(0);
			for(Order order: sale.getProductOrders())
			{
				addRow(order);
			}
			
		}
		@Override
		public void setSubject(InvoiceObservable observable) {
			this.observable  = observable;
			
		}

}
