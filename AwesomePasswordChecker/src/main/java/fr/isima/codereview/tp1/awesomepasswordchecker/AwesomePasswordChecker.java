/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package fr.isima.codereview.tp1.awesomepasswordchecker;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * This class manages the calculation of password strength. 
 * It analyzes the characteristics of a password and returns a strength score
 * It can also calculate the distance between a password and a set of cluster centers.
 * 
 * @author NDAO Aly
 * @version 2024-11-18
*/
public class AwesomePasswordChecker {

  private static AwesomePasswordChecker instance;

  private final List<double[]> clusterCenters = new ArrayList<>();

/**
 * Returns the singleton instance of the AwesomePasswordChecker, initializing it with the given file.
 * If the instance does not exist, it creates a new one using the provided file to load the necessary data.
 *
 * @param file The file containing the necessary data to initialize the AwesomePasswordChecker instance.
 * @return The singleton instance of AwesomePasswordChecker.
 * @throws IOException If an error occurs while reading the file.
 */
  public static AwesomePasswordChecker getInstance(File file) throws IOException {
    if (instance == null) {
          instance = new AwesomePasswordChecker(new FileInputStream(file));
    }
    return instance;
  }
  
/**
 * Returns the singleton instance of the AwesomePasswordChecker, initializing it with data from a resource file.
 * If the instance does not exist, it creates a new one by loading data from the "cluster_centers_HAC_aff.csv" resource.
 *
 * @return The singleton instance of AwesomePasswordChecker.
 * @throws IOException If an error occurs while reading the resource file.
 */
  public static AwesomePasswordChecker getInstance() throws IOException {
    if (instance == null) {
      InputStream is = AwesomePasswordChecker.class.getClassLoader().getResourceAsStream("cluster_centers_HAC_aff.csv");
      instance = new AwesomePasswordChecker(is);
    }
      return instance;
  }
 
 /**
  * Constructs an instance of AwesomePasswordChecker by loading cluster center data from the given input stream.
  * This constructor reads each line of the input stream, splits the data by commas, and stores the parsed values
  * as cluster centers to be used for password strength calculations.
  *
  * @param is The input stream containing the cluster center data (typically a CSV file).
  * @throws IOException If an error occurs while reading the input stream.
  */  
  private AwesomePasswordChecker(InputStream is) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(is));
    String line;
    while((line = br.readLine()) != null){
      String[] values = line.split(",");
      double[] center = new double[values.length];
      
      for (int i = 0; i < values.length; ++i) {
        center[i] = Double.parseDouble(values[i]);
      }
      clusterCenters.add(center);
    }
    br.close();
  }

 
 /**
  * Generates a mask array based on the characters in the given password.
  * Each character in the password is mapped to a specific integer value according to predefined rules:
  * - Vowels and some common consonants are mapped to 1 or 3.
  * - Special characters are mapped to 6.
  * - Lowercase letters are mapped to 2, uppercase letters to 4, digits to 5, and other characters to 7.
  * The resulting mask array helps represent the character characteristics of the password.
  *
  * @param password The password string for which the mask is generated.
  * @return An integer array of length 28 representing the mask of the password, where each element corresponds to a character in the password.
  *         The array will be filled with values based on the character categories defined in the method.
  */
  public int[] maskAff(String password) {
    int[] maskArray = new int[28]; 
    int limit = Math.min(password.length(), 28);
    
    for (int i = 0; i < limit; ++i) {
          char c = password.charAt(i);
      switch (c) {
        case 'e': 
        case 's':
        case 'a':
        case 'i':
        case 't':
        case 'n':
        case 'r':
        case 'u':
        case 'o':
        case 'l':
            maskArray[i] = 1;
          break;
        case 'E':
        case 'S':
        case 'A':
        case 'I':
        case 'T':
        case 'N':
        case 'R':
        case 'U':
        case 'O':
        case 'L':
          maskArray[i] = 3;
          break;
        case '>':
        case '<':
        case '-':
        case '?':
        case '.':
        case '/':
        case '!':
        case '%':
        case '@':
        case '&':
          maskArray[i] = 6;
          break;
        default:
          if (Character.isLowerCase(c)) {
            maskArray[i] = 2;
          } else if (Character.isUpperCase(c)) {
            maskArray[i] = 4;
          } else if (Character.isDigit(c)) {
            maskArray[i] = 5;
          } else {
            maskArray[i] = 7;
          }
      }
    }
    return maskArray;
  }

 /**
  * Calculates the minimum Euclidean distance between the given password and a set of cluster centers.
  * This method generates a mask for the password, then computes the Euclidean distance between 
  * the generated mask and each cluster center. It returns the smallest distance found.
  *
  * @param password The password for which the distance is calculated.
  * @return The minimum Euclidean distance between the generated mask and the cluster centers.
  */
  public double getDIstance(String password) {
    int[] maskArray = maskAff(password);
    double minDistance = Double.MAX_VALUE;
    for (double[] center : clusterCenters) {
      minDistance = Math.min(euclideanDistance(maskArray, center), minDistance);
    }
    return minDistance;
  }

  
 /**
  * Calculates the Euclidean distance between two arrays.
  * This method takes two arrays, one of integers and one of doubles, 
  * and calculates the difference between corresponding elements in each array.
  *
  * @param a An integer array representing the first data set.
  * @param b A double array representing the second data set.
  * @return The Euclidean distance between the two arrays.
  */
  private double euclideanDistance(int[] a, double[] b) {
    double sum = 0;
    for (int i = 0; i < a.length; i++) {
       sum += Math.pow(a[i] - b[i], 2);
    }
    return Math.sqrt(sum);
  }

  
  
 /**
  * Computes the MD5 hash of the given input string.
  * This method implements the MD5 algorithm step-by-step, padding the input message, 
  * processing it in blocks, and applying the MD5 transformation rounds to generate the hash.
  * 
  * The resulting MD5 hash is returned as a 32-character hexadecimal string.
  *
  * @param input The input string for which the MD5 hash is computed.
  * @return The 32-character hexadecimal string representing the MD5 hash of the input.
  * 
  * @throws IllegalArgumentException If the input is null.
  */
  public static String ComputeMD5(String input) {
    byte[] message = input.getBytes();
    int messageLenBytes = message.length;

    int numBlocks = ((messageLenBytes + 8) >>> 6) + 1;
    int totalLen = numBlocks << 6;
    byte[] paddingBytes = new byte[totalLen - messageLenBytes];
    paddingBytes[0] = (byte) 0x80;

    long messageLenBits = (long) messageLenBytes << 3;
    ByteBuffer lengthBuffer = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(messageLenBits);
    byte[] lengthBytes = lengthBuffer.array();

    byte[] paddedMessage = new byte[totalLen];
    System.arraycopy(message, 0, paddedMessage, 0, messageLenBytes);
    System.arraycopy(paddingBytes, 0, paddedMessage, messageLenBytes, paddingBytes.length);
    System.arraycopy(lengthBytes, 0, paddedMessage, totalLen - 8, 8);

    int[] h = {
      0x67452301,
      0xefcdab89,
      0x98badcfe,
      0x10325476
    };

    int[] k = {
      0xd76aa478, 0xe8c7b756, 0x242070db, 0xc1bdceee, 0xf57c0faf, 0x4787c62a, 0xa8304613, 0xfd469501,
      0x698098d8, 0x8b44f7af, 0xffff5bb1, 0x895cd7be, 0x6b901122, 0xfd987193, 0xa679438e, 0x49b40821,
      0xf61e2562, 0xc040b340, 0x265e5a51, 0xe9b6c7aa, 0xd62f105d, 0x02441453, 0xd8a1e681, 0xe7d3fbc8,
      0x21e1cde6, 0xc33707d6, 0xf4d50d87, 0x455a14ed, 0xa9e3e905, 0xfcefa3f8, 0x676f02d9, 0x8d2a4c8a,
      0xfffa3942, 0x8771f681, 0x6d9d6122, 0xfde5380c, 0xa4beea44, 0x4bdecfa9, 0xf6bb4b60, 0xbebfbc70,
      0x289b7ec6, 0xeaa127fa, 0xd4ef3085, 0x04881d05, 0xd9d4d039, 0xe6db99e5, 0x1fa27cf8, 0xc4ac5665,
      0xf4292244, 0x432aff97, 0xab9423a7, 0xfc93a039, 0x655b59c3, 0x8f0ccc92, 0xffeff47d, 0x85845dd1,
      0x6fa87e4f, 0xfe2ce6e0, 0xa3014314, 0x4e0811a1, 0xf7537e82, 0xbd3af235, 0x2ad7d2bb, 0xeb86d391
    };

    int[] r = {
      7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22,
      5, 9, 14, 20, 5, 9, 14, 20, 5, 9, 14, 20, 5, 9, 14, 20,
      4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23,
      6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21
    };

    for (int i = 0; i < numBlocks; i++) {
      int[] w = new int[16];
      for (int j = 0; j < 16; j++) {
        w[j] = ByteBuffer.wrap(paddedMessage, (i << 6) + (j << 2), 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
      }

      int a = h[0];
      int b = h[1];
      int c = h[2];
      int d = h[3];

      for (int j = 0; j < 64; j++) {
        int f, g;
        if (j < 16) {
          f = (b & c) | (~b & d);
          g = j;
        } else if (j < 32) {
          f = (d & b) | (~d & c);
          g = (5 * j + 1) % 16;
        } else if (j < 48) {
          f = b ^ c ^ d;
          g = (3 * j + 5) % 16;
        } else {
          f = c ^ (b | ~d);
          g = (7 * j) % 16;
        }
        int temp = d;
        d = c;
        c = b;
        b = b + Integer.rotateLeft(a + f + k[j] + w[g], r[j]);
        a = temp;
      }

      h[0] += a;
      h[1] += b;
      h[2] += c;
      h[3] += d;
    }

    // Step 5: Output
    ByteBuffer md5Buffer = ByteBuffer.allocate(16).order(ByteOrder.LITTLE_ENDIAN);
    md5Buffer.putInt(h[0]).putInt(h[1]).putInt(h[2]).putInt(h[3]);
    byte[] md5Bytes = md5Buffer.array();

    StringBuilder md5Hex = new StringBuilder();
    for (byte b : md5Bytes) {
      md5Hex.append(String.format("%02x", b));
    }

    return md5Hex.toString();
  }
  
    public static void main(String[] args) {
      System.out.println("Bienvenue dans AwesomePasswordChecker !");

      try {
          // Utilisation de la méthode getInstance pour créer l'instance
          AwesomePasswordChecker checker = AwesomePasswordChecker.getInstance();
  
          // Test 1 : Génération du masque pour le mot de passe
          System.out.println("\nTest 1 : Génération du masque pour le mot de passe");
          int[] mask = checker.maskAff("Test123!");

          // Affichage du tableau de masques généré pour le mot de passe
          System.out.print("Tableau de masque : ");
          for (int value : mask) {
              System.out.print(value + " ");
          }
          System.out.println();
          
          
           // Test 2 : Calcul de la distance pour le mot de passe donné
        System.out.println("\nTest 2 : Calcul de la distance pour le mot de passe");
        String password = "Test123!";
        double distance = checker.getDIstance(password);

        // Affichage de la distance minimale
        System.out.println("Distance minimale pour le mot de passe '" + password + "' : " + distance);
        
        String md5Hash = AwesomePasswordChecker.ComputeMD5(password);

        // Affichage du résultat
        System.out.println("MD5 de " + password + " : " + md5Hash);
          
      } catch (IOException e) {
          System.out.println("Une erreur s'est produite lors du chargement des centres de clusters : " + e.getMessage());
      }
}

}
