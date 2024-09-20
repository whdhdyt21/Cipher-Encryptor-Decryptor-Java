# Cipher Encryptor Decryptor Java

This Java application provides a graphical user interface for encrypting and decrypting messages using three different cipher algorithms: Vigenère, Playfair, and Hill cipher.

## Features

- User-friendly GUI built with Java Swing
- Support for three cipher algorithms:
  - Vigenère Cipher
  - Playfair Cipher
  - Hill Cipher
- Ability to input messages directly or upload from a .txt file
- Encryption and decryption functionality
- Customizable encryption key (minimum 12 characters)

## Requirements

- Java Development Kit (JDK) 8 or higher

## How to Run

1. Compile the Java files:
   ```
   javac *.java
   ```
2. Run the main class:
   ```
   java CipherAppSwing
   ```

## Usage

1. Enter your message in the "Message" field or use the "Upload .txt File" button to load a message from a text file.
2. Enter a key of at least 12 characters in the "Key" field.
3. Select the desired cipher algorithm from the dropdown menu.
4. Click "Encrypt" to encrypt your message or "Decrypt" to decrypt a message.
5. The result will be displayed in the "Result" area.

## Code Structure

- `CipherAppSwing.java`: Contains the main method and GUI implementation.
- `CipherUtils.java`: Implements the encryption and decryption algorithms for all three ciphers.

## Contributing

Contributions, issues, and feature requests are welcome. Feel free to check [issues page](https://github.com/yourusername/cipher-encryptor-decryptor-java/issues) if you want to contribute.

## License

[MIT](https://choosealicense.com/licenses/mit/)