package org.upf.ac.ma.IBM.GraphicInterface;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.awt.Font;
import java.util.Arrays;

public class CheckMask extends JFrame {
	File file;
	
	public CheckMask() {
		setTitle("IBM Watson Studio");
		setSize(700, 500);
		getContentPane().setLayout(null);
        this.setLocationRelativeTo(null);  // <<--- plain and simple

		
		JLabel lblNewLabel = new JLabel("Verification du Masque ");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblNewLabel.setBounds(253, 30, 278, 28);
		getContentPane().add(lblNewLabel);
		
		final JLabel imagelbl = new JLabel("");
		imagelbl.setBounds(211, 60, 231, 169);
		getContentPane().add(imagelbl);
		
		
		JButton btnNewButton = new JButton("telecharger Votre Photo Pour le Traiter");
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser filechooser = new JFileChooser();
			    filechooser.setDialogTitle("Choose Your File");
			    filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			    // below code selects the file 
			    int returnval = filechooser.showSaveDialog(null);
			    if (returnval == JFileChooser.APPROVE_OPTION)
			    {
			        file = filechooser.getSelectedFile();
			        BufferedImage bi;
			        try {
			            // display the image in a Jlabel
			            bi = ImageIO.read(file);
			            java.awt.Image dimg = bi.getScaledInstance(imagelbl.getWidth(), imagelbl.getHeight(),bi.SCALE_SMOOTH);
			            imagelbl.setIcon(new ImageIcon(dimg));
			        } catch(IOException t) {
			           t.printStackTrace(); // todo: implement proper error handeling
			        }
			        
			    }
			}
		});
		btnNewButton.setBounds(211, 240, 288, 50);
		getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Verifier");
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//System.out.println(file);
				if(file!=null) {
					
					
					
					
					System.out.println(file);
					System.out.println(file.getName());
			        IamOptions options = new IamOptions.Builder()
	        		.apiKey("DZ49k1hV6QPmabP9n8S00c0IN1XDvCKRJnU9oS8fG7zt")
	        		.build();
	        VisualRecognition service = new VisualRecognition("2018-03-19", options);
	
	        InputStream imagesStream;
			try {
				imagesStream = new FileInputStream(file.toString());
				ClassifyOptions classifyOptions = new ClassifyOptions.Builder()
			        	.imagesFile(imagesStream)
			        	.imagesFilename(file.getName())
			        	.threshold((float) 0.6)
			        	.classifierIds(Arrays.asList("YoussefClass_437944188"))
			        	.build();
			
			        ClassifiedImages result = service.classify(classifyOptions).execute();
			        
			        JSONObject jsonObject = new JSONObject(result);
			        System.out.println(result);
			        JSONArray jsonclas = new JSONArray(jsonObject.getJSONArray("images"));
			        if(jsonclas.isEmpty()) {
			        	System.out.println("vide");
			        	
			        }
			        else {
			        	System.out.println("non vide");
			        	JSONObject jsonextract = (JSONObject) jsonclas.get(0);
			        	System.out.println(jsonextract.getString("image"));
			        	JSONArray jsonar = new JSONArray(jsonextract.getJSONArray("classifiers"));
			        	JSONObject jsonavantder =(JSONObject) jsonar.get(0);
			        	System.out.println(jsonavantder.getString("name"));
			        	
			        	JSONArray jsonarFinale = new JSONArray(jsonavantder.getJSONArray("classes"));
			        	JSONObject jsonobjdernier = (JSONObject) jsonarFinale.get(0);
			        	System.out.println("resultat : "+jsonobjdernier.getString("className"));
			        	System.out.println("resultat : "+jsonobjdernier.getFloat("score"));
			        	
			        	Float porcentage = (jsonobjdernier.getFloat("score")*100);
			        	if(jsonobjdernier.getString("className").equals("Sans.zip")) {
					        JOptionPane.showMessageDialog(null, "Dommage la Personne ne porte pas du masque \nle Pourcentage du traitement est : "+porcentage+"%", "Reponse : IBM Watson Studio" , JOptionPane.ERROR_MESSAGE);
			        	}
			        	else {
					        JOptionPane.showMessageDialog(null, "Parfait la Personne porte du masque \nle Pourcentage du traitement est : "+porcentage+"%", "Reponse : IBM Watson Studio" , JOptionPane.INFORMATION_MESSAGE);
			        	}
			        }

			        
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        
	        
	        
	        
	        
	        
	        
	        
				}else {
			        JOptionPane.showMessageDialog(null, "Selectionner une Image Avant de Verifier", "Message d'erreur : IBM Watson Studio" , JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		btnNewButton_1.setBounds(284, 318, 143, 39);
		getContentPane().add(btnNewButton_1);
		
	}
}
