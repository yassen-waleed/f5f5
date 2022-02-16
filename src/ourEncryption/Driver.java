package ourEncryption;

import javax.swing.JOptionPane;
import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import ourEncryption.Huffman.*;
import ourEncryption.RC4.RC4;
import ourEncryption.Vigenere.VigenereCipher;
import ourEncryption.hillCipher.Hill;

public class Driver extends Application {

    File planeFile;
    File fileToEncrypt;


    File encryptedFile;
    File fileToDecrypt;


    @Override
    public void start(Stage stage) {

        stage.getIcons().add(new Image("file:face.png"));
        stage.setTitle("Encryption Project");
        //stage.resizableProperty().setValue(Boolean.FALSE);

        Label ourEncryptionLabel = new Label("Our Encrypt");
        ourEncryptionLabel.setFont(new Font("Algerian", 34));
        ourEncryptionLabel.setTextFill(Color.WHITE);
        ourEncryptionLabel.setAlignment(Pos.TOP_CENTER);

        Label encryptLabel = new Label("Encrypt");
        encryptLabel.setFont(new Font("Algerian", 24));
        encryptLabel.setTextFill(Color.WHITE);

        Label decrypt = new Label("Decrypt");
        decrypt.setFont(new Font("Algerian", 24));
        decrypt.setTextFill(Color.WHITE);

        Button close = new Button("Exit");
        close.setTextFill(Color.RED);
        close.setTooltip(new Tooltip("exit from application"));
        close.setPrefWidth(100);
        close.setFont(new Font("Copperplate Gothic Bold", 12));

        Button fileChooserForPlaneFile = new Button("  Choose Plane File  ");
        fileChooserForPlaneFile.setTextFill(Color.BLACK);
        fileChooserForPlaneFile.setFont(new Font("Courier New", 15));

        Button fileChooserForEncryptedFile = new Button(" Save Encrypted File ");
        fileChooserForEncryptedFile.setTextFill(Color.BLACK);
        fileChooserForEncryptedFile.setFont(new Font("Courier New", 15));

        Button fileChooserForEncryptedFileToDecrypt = new Button("Choose Encrypted File");
        fileChooserForEncryptedFileToDecrypt.setFont(new Font("Courier New", 15));

        Button fileChooserForDecryptedFileToDecrypt = new Button(" Save Decrypted File ");
        fileChooserForDecryptedFileToDecrypt.setFont(new Font("Courier New", 15));

        Button encryptButton = new Button("Encrypt");
        Button decryptButton = new Button("Decrypt");


        VBox encryptVBox = new VBox(20, encryptLabel, fileChooserForPlaneFile, fileChooserForEncryptedFile, encryptButton);
        VBox decryptVBox = new VBox(20, decrypt, fileChooserForEncryptedFileToDecrypt, fileChooserForDecryptedFileToDecrypt, decryptButton);

        HBox hBox = new HBox(70, encryptVBox, decryptVBox);

        VBox primaryVBox = new VBox(60, ourEncryptionLabel, hBox, close);
        primaryVBox.setBorder(new Border(
                new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        // set Alignment
        primaryVBox.setAlignment(Pos.CENTER);
        Image backgroundImage = new Image("file:image.jpg");
        BackgroundImage backgroundimage = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background background = new Background(backgroundimage);
        primaryVBox.setBackground(background);

        hBox.setAlignment(Pos.CENTER);

        decryptVBox.setAlignment(Pos.CENTER);

        encryptVBox.setAlignment(Pos.CENTER);

        // create a File chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"),
                new ExtensionFilter("Text Files", "*.txt"), new ExtensionFilter("Java Files", "*.java"),
                new ExtensionFilter("Web Files", "*.html", "*.css", "*.js", "*.php"),
                new ExtensionFilter("Image files", "*.png", "*.jpg"), new ExtensionFilter("Word files", "*.docx"),
                new ExtensionFilter("Pdf files", "*.pdf")
        );

        // create a File chooser
        FileChooser ourFilesChooser = new FileChooser();
        ourFilesChooser.getExtensionFilters().add(new ExtensionFilter("Encrypted Files", "*.our"));

        fileChooserForPlaneFile.setOnAction(e -> {
            planeFile = fileChooser.showOpenDialog(stage);// select file to compress
        });

        fileChooserForEncryptedFile.setOnAction(e -> {
            fileToEncrypt = ourFilesChooser.showSaveDialog(stage);// select file to compress
        });

        fileChooserForEncryptedFileToDecrypt.setOnAction(e -> {
            encryptedFile = ourFilesChooser.showOpenDialog(stage);// selected file to decompress.
        });

        fileChooserForDecryptedFileToDecrypt.setOnAction(e -> {
            fileToDecrypt = fileChooser.showSaveDialog(stage);// select file to compress
        });

        close.setOnAction(e -> {
            JOptionPane.showMessageDialog(null, "Good Bye");
            System.exit(0);
        });

        encryptButton.setOnAction(e -> {
            if (planeFile == null || fileToEncrypt == null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You Need To Choose The Files", ButtonType.CANCEL);
                alert.showAndWait();
            } else {
                try {
                    ourEncryption(planeFile, fileToEncrypt);
                    JOptionPane.showMessageDialog(null, "File Has Been Encrypted");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        decryptButton.setOnAction(e -> {
            if (encryptedFile == null || fileToDecrypt == null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You Need To Choose The Files", ButtonType.CANCEL);
                alert.showAndWait();
            } else {
                try {
                    ourDecryption(encryptedFile, fileToDecrypt);
                    JOptionPane.showMessageDialog(null, "File Has Been Decrypted");

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        StackPane stp = new StackPane(primaryVBox);

        Scene scene = new Scene(stp, 600, 500);
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void ourDecryption(File ef, File ftd) throws IOException {
        File temp  =  new File("./temp") ;
        RC4.encryption_decryption(ef, temp);
        VigenereCipher.decrypt(temp, temp);
        HuffmanEncryption.decrypt(temp, ftd);
         temp.delete() ;
    }

    public static void ourEncryption(File planeFile, File fileToEncrypt) throws IOException {
        File temp  =  new File("./temp") ;
        HuffmanEncryption.encrypt(planeFile, temp);
        VigenereCipher.encrypt(temp,temp);
        RC4.encryption_decryption(temp  , fileToEncrypt);
        temp.delete() ;
    }


}
