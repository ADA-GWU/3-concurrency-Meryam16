**Concurrency - Image Processing Java Project**

This Java project showcases concurrent image processing, offering both single-threaded and multithreaded modes. The program reads an image, applies pixelation, and then preserves the pixelated output as a new image.


## Prerequisites
    - Java Development Kit (JDK)


**Running the Application**

    - javac -sourcepath ./src/ -d ./bin/ ./src/Main.java
    - java -cp ./bin/ Main image.jpg 50 S

**How to Use**

    - After running the application, it will prompt you for input. Enter filename, square size and mode (S for single-threaded or M for multi-threaded).
    - The application will process the image and display the result on the screen.
    - Once processing is complete, a pixelated image will be saved as "result.jpg" in the 'src/main/resources' directory.

**Project Structure**

    - 'Main.java' : The entry point of the application that handles user input and mode selection.
    - 'ImageProcessingSingle.java' : Single-threaded image processing class, pixelating an image using a timer.
    - 'ImageProcessingMulti.java' : Multi-threaded image processing class, dividing the image into regions and processing them concurrently.


