package orig;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main extends Application {
    private Stage primaryStage;
    private Group root;
    private TextArea tm; //time

    private ImageView[][] field = new ImageView[28][20]; //actual field
    private int[][] props = new int[28][20]; //props field
    private int[][] orgn = new int[28][20]; // (coveredEmpty = 1 & coveredMine = 2 only) to check for win condition

    private int xxx = 0; //shake timeline coords
    private int yyy = 0;
    private int cnt; //counter of # of mines round a spot to generate # tag
    private int flags = 0; //# of flags put
    private int numOfMines = 0; //=90

    //the diff states of each spot
    private int coveredEmpty = 1, coveredMine = 2; // revealedMine = 5
    private int revealedEmpty = 4, revealedNumber = 6;

    private int coveredNumber1 = 31, coveredNumber2 = 32, coveredNumber3 = 33, coveredNumber4 = 34;
    private int coveredNumber5 = 35, coveredNumber6 = 36, coveredNumber7 = 37, coveredNumber8 = 38;

    private int coveredNumber1Flag = 71, coveredNumber2Flag = 72, coveredNumber3Flag = 73, coveredNumber4Flag = 74;
    private int coveredNumber5Flag = 75, coveredNumber6Flag = 76, coveredNumber7Flag = 77, coveredNumber8Flag = 78;
    private int coveredEmptyFlag = 79, coveredMineFlag = 70;

    private TextArea over; //game over

    // # tags urls
    private String urlNum1 = "file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\1.png";
    private String urlNum2 = "file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\2.png";
    private String urlNum3 = "file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\3.png";
    private String urlNum4 = "file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\4.png";
    private String urlNum5 = "file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\5.png";
    private String urlNum6 = "file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\6.png";
    private String urlNum7 = "file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\7.png";
    private String urlNum8 = "file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\8.png";

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        primaryStage.setTitle("MINESWEEPER");
        root = new Group();

        //title text
        TextArea title = new TextArea();
        title.setText("  Minesweeper");
        title.setTranslateX(750);
        title.setTranslateY(25);
        title.setPrefHeight(25);
        title.setPrefWidth(100);
        title.setEditable(false);

        //flags counter text
        TextArea mines = new TextArea();
        mines.setText("Flags: " + flags);
        mines.setTranslateX(750);
        mines.setTranslateY(115);
        mines.setPrefHeight(25);
        mines.setPrefWidth(100);
        mines.setEditable(false);

        //total mines # text
        TextArea numMines = new TextArea();
        numMines.setText("Mines: 90");
        numMines.setTranslateX(750);
        numMines.setTranslateY(70);
        numMines.setPrefHeight(25);
        numMines.setPrefWidth(100);
        numMines.setEditable(false);

        //time text
        tm = new TextArea();
        tm.setTranslateX(750);
        tm.setTranslateY(205);
        tm.setPrefHeight(25);
        tm.setPrefWidth(100);
        tm.setEditable(false);

        //game over text
        over = new TextArea();
        over.setText("");
        over.setTranslateX(750);
        over.setTranslateY(160);
        over.setPrefHeight(25);
        over.setPrefWidth(100);
        over.setEditable(false);

        //covered spot (no flag) url
        String url = "file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\blueOrig.png";

        //time thread
        Thread timerThread = new Thread(() -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                String time = simpleDateFormat.format(new Date());
                Platform.runLater(() -> tm.setText(time));
            }
        });

        timerThread.start();

        //raw field setup (viewimages) with events
        for(int y = 0; y < 20; y++) {
            for (int x = 0; x < 28; x++) {
                ImageView image = new ImageView();
                image.setImage(new Image(url));
                image.setFitWidth(25);
                image.setFitHeight(25);
                image.setTranslateX(25 + x * 25);
                image.setTranslateY(25 + y * 25);
                root.getChildren().add(image);


                int finalX = x;
                int finalY = y;
                image.setOnMouseClicked(event -> {
                    if(event.getButton() == MouseButton.SECONDARY) {

                        //put flag
                            if (props[finalX][finalY] == coveredMine) {
                                image.setImage(new Image("file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\flag.png"));
                                props[finalX][finalY] = coveredMineFlag;
                                flags++;
                                mines.setText("Flags: " + flags);
                            } else if (props[finalX][finalY] == coveredEmpty) {
                                image.setImage(new Image("file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\flag.png"));
                                props[finalX][finalY] = coveredEmptyFlag;
                                flags++;
                                mines.setText("Flags: " + flags);
                            } else if (props[finalX][finalY] == coveredNumber1) {
                                image.setImage(new Image("file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\flag.png"));
                                props[finalX][finalY] = coveredNumber1Flag;
                                flags++;
                                mines.setText("Flags: " + flags);
                            } else if (props[finalX][finalY] == coveredNumber2) {
                                image.setImage(new Image("file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\flag.png"));
                                props[finalX][finalY] = coveredNumber2Flag;
                                flags++;
                                mines.setText("Flags: " + flags);
                            } else if (props[finalX][finalY] == coveredNumber3) {
                                image.setImage(new Image("file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\flag.png"));
                                props[finalX][finalY] = coveredNumber3Flag;
                                flags++;
                                mines.setText("Flags: " + flags);
                            } else if (props[finalX][finalY] == coveredNumber4) {
                                image.setImage(new Image("file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\flag.png"));
                                props[finalX][finalY] = coveredNumber4Flag;
                                flags++;
                                mines.setText("Flags: " + flags);
                            } else if (props[finalX][finalY] == coveredNumber5) {
                                image.setImage(new Image("file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\flag.png"));
                                props[finalX][finalY] = coveredNumber5Flag;
                                flags++;
                                mines.setText("Flags: " + flags);
                            } else if (props[finalX][finalY] == coveredNumber6) {
                                image.setImage(new Image("file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\flag.png"));
                                props[finalX][finalY] = coveredNumber6Flag;
                                flags++;
                                mines.setText("Flags: " + flags);
                            } else if (props[finalX][finalY] == coveredNumber7) {
                                image.setImage(new Image("file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\flag.png"));
                                props[finalX][finalY] = coveredNumber7Flag;
                                flags++;
                                mines.setText("Flags: " + flags);
                            } else if (props[finalX][finalY] == coveredNumber8) {
                                image.setImage(new Image("file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\flag.png"));
                                props[finalX][finalY] = coveredNumber8Flag;
                                flags++;
                                mines.setText("Flags: " + flags);
                            }

                            //remove flag
                            else if (props[finalX][finalY] == coveredMineFlag) {
                                image.setImage(new Image("file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\blueOrig.png"));
                                props[finalX][finalY] = coveredMine;
                                flags--;
                                mines.setText("Flags: " + flags);
                            } else if (props[finalX][finalY] == coveredEmptyFlag) {
                                image.setImage(new Image("file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\blueOrig.png"));
                                props[finalX][finalY] = coveredEmpty;
                                flags--;
                                mines.setText("Flags: " + flags);
                            } else if (props[finalX][finalY] == coveredNumber1Flag) {
                                image.setImage(new Image("file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\blueOrig.png"));
                                props[finalX][finalY] = coveredNumber1;
                                flags--;
                                mines.setText("Flags: " + flags);
                            } else if (props[finalX][finalY] == coveredNumber2Flag) {
                                image.setImage(new Image("file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\blueOrig.png"));
                                props[finalX][finalY] = coveredNumber2;
                                flags--;
                                mines.setText("Flags: " + flags);
                            } else if (props[finalX][finalY] == coveredNumber3Flag) {
                                image.setImage(new Image("file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\blueOrig.png"));
                                props[finalX][finalY] = coveredNumber3;
                                flags--;
                                mines.setText("Flags: " + flags);
                            } else if (props[finalX][finalY] == coveredNumber4Flag) {
                                image.setImage(new Image("file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\blueOrig.png"));
                                props[finalX][finalY] = coveredNumber4;
                                flags--;
                                mines.setText("Flags: " + flags);
                            } else if (props[finalX][finalY] == coveredNumber5Flag) {
                                image.setImage(new Image("file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\blueOrig.png"));
                                props[finalX][finalY] = coveredNumber5;
                                flags--;
                                mines.setText("Flags: " + flags);
                            } else if (props[finalX][finalY] == coveredNumber6Flag) {
                                image.setImage(new Image("file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\blueOrig.png"));
                                props[finalX][finalY] = coveredNumber6;
                                flags--;
                                mines.setText("Flags: " + flags);
                            } else if (props[finalX][finalY] == coveredNumber7Flag) {
                                image.setImage(new Image("file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\blueOrig.png"));
                                props[finalX][finalY] = coveredNumber7;
                                flags--;
                                mines.setText("Flags: " + flags);
                            } else if (props[finalX][finalY] == coveredNumber8Flag) {
                                image.setImage(new Image("file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\blueOrig.png"));
                                props[finalX][finalY] = coveredNumber8;
                                flags--;
                                mines.setText("Flags: " + flags);
                            }
                            //if(flags == 15){System.out.println(Arrays.deepToString(props));
                            //}
                    }

                    if(event.getButton() == MouseButton.PRIMARY) {
                        //mine revealed, GAME OVER
                        if(props[finalX][finalY] == coveredMine) {
                            image.setImage(new Image("file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\mine.png"));
                            over.setText("GAME OVER");
                            for (int y1 = 0; y1 < 20; y1++) {
                                for (int x1 = 0; x1 < 28; x1++) {
                                    field[x1][y1].setDisable(true);
                                }
                            }
                            shakeStage(primaryStage);
                        }

                        //reveal round a clicked-on revealed number
                            if (props[finalX][finalY] == revealedNumber) {
                                if(finalX != 0 && finalY != 0) {
                                    if (props[finalX - 1][finalY - 1] == coveredEmpty) {
                                        props[finalX - 1][finalY - 1] = revealedEmpty;
                                        field[finalX - 1][finalY - 1].setImage(new Image("file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\empty.png"));
                                        revEmptArea(finalX - 1, finalY - 1);
                                    }
                                    if (props[finalX - 1][finalY - 1] == coveredMine) {
                                        endGame(finalX - 1, finalY - 1);
                                    }
                                    if (props[finalX - 1][finalY - 1] == coveredNumber1 || props[finalX - 1][finalY - 1] == coveredNumber2 || props[finalX - 1][finalY - 1] == coveredNumber3 ||
                                            props[finalX - 1][finalY - 1] == coveredNumber4 || props[finalX - 1][finalY - 1] == coveredNumber5 || props[finalX - 1][finalY - 1] == coveredNumber6 ||
                                            props[finalX - 1][finalY - 1] == coveredNumber7 || props[finalX - 1][finalY - 1] == coveredNumber8) {
                                        opn(finalX - 1, finalY - 1, props[finalX - 1][finalY - 1]);
                                    }
                                }


                                if(finalX != 0) {
                                    if (props[finalX - 1][finalY] == coveredEmpty) {
                                        props[finalX - 1][finalY] = revealedEmpty;
                                        field[finalX - 1][finalY].setImage(new Image("file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\empty.png"));
                                        revEmptArea(finalX - 1, finalY);
                                    }
                                    if (props[finalX - 1][finalY] == coveredMine) {
                                        endGame(finalX - 1, finalY);
                                    }
                                    if (props[finalX - 1][finalY] == coveredNumber1 || props[finalX - 1][finalY] == coveredNumber2 || props[finalX - 1][finalY] == coveredNumber3 ||
                                            props[finalX - 1][finalY] == coveredNumber4 || props[finalX - 1][finalY] == coveredNumber5 || props[finalX - 1][finalY] == coveredNumber6 ||
                                            props[finalX - 1][finalY] == coveredNumber7 || props[finalX - 1][finalY] == coveredNumber8) {
                                        opn(finalX - 1, finalY, props[finalX - 1][finalY]);
                                    }
                                }


                                if(finalX != 0 && finalY != 19) {
                                    if (props[finalX - 1][finalY + 1] == coveredEmpty) {
                                        props[finalX - 1][finalY + 1] = revealedEmpty;
                                        field[finalX - 1][finalY + 1].setImage(new Image("file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\empty.png"));
                                        revEmptArea(finalX - 1, finalY + 1);
                                    }
                                    if (props[finalX - 1][finalY + 1] == coveredMine) {
                                        endGame(finalX - 1, finalY + 1);
                                    }
                                    if (props[finalX - 1][finalY + 1] == coveredNumber1 || props[finalX - 1][finalY + 1] == coveredNumber2 || props[finalX - 1][finalY + 1] == coveredNumber3 ||
                                            props[finalX - 1][finalY + 1] == coveredNumber4 || props[finalX - 1][finalY + 1] == coveredNumber5 || props[finalX - 1][finalY + 1] == coveredNumber6 ||
                                            props[finalX - 1][finalY + 1] == coveredNumber7 || props[finalX - 1][finalY + 1] == coveredNumber8) {
                                        opn(finalX - 1, finalY + 1, props[finalX - 1][finalY + 1]);
                                    }
                                }


                                if(finalY != 0) {
                                    if (props[finalX][finalY - 1] == coveredEmpty) {
                                        props[finalX][finalY - 1] = revealedEmpty;
                                        field[finalX][finalY - 1].setImage(new Image("file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\empty.png"));
                                        revEmptArea(finalX, finalY - 1);
                                    }
                                    if (props[finalX][finalY - 1] == coveredMine) {
                                        endGame(finalX, finalY - 1);
                                    }
                                    if (props[finalX][finalY - 1] == coveredNumber1 || props[finalX][finalY - 1] == coveredNumber2 || props[finalX][finalY - 1] == coveredNumber3 ||
                                            props[finalX][finalY - 1] == coveredNumber4 || props[finalX][finalY - 1] == coveredNumber5 || props[finalX][finalY - 1] == coveredNumber6 ||
                                            props[finalX][finalY - 1] == coveredNumber7 || props[finalX][finalY - 1] == coveredNumber8) {
                                        opn(finalX, finalY - 1, props[finalX][finalY - 1]);
                                    }
                                }


                                if(finalY != 19) {
                                    if (props[finalX][finalY + 1] == coveredEmpty) {
                                        props[finalX][finalY + 1] = revealedEmpty;
                                        field[finalX][finalY + 1].setImage(new Image("file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\empty.png"));
                                        revEmptArea(finalX, finalY + 1);
                                    }
                                    if (props[finalX][finalY + 1] == coveredMine) {
                                        endGame(finalX, finalY + 1);
                                    }
                                    if (props[finalX][finalY + 1] == coveredNumber1 || props[finalX][finalY + 1] == coveredNumber2 || props[finalX][finalY + 1] == coveredNumber3 ||
                                            props[finalX][finalY + 1] == coveredNumber4 || props[finalX][finalY + 1] == coveredNumber5 || props[finalX][finalY + 1] == coveredNumber6 ||
                                            props[finalX][finalY + 1] == coveredNumber7 || props[finalX][finalY + 1] == coveredNumber8) {
                                        opn(finalX, finalY + 1, props[finalX][finalY + 1]);
                                    }
                                }


                                if(finalX != 27 && finalY != 0) {
                                    if (props[finalX + 1][finalY - 1] == coveredEmpty) {
                                        props[finalX + 1][finalY - 1] = revealedEmpty;
                                        field[finalX + 1][finalY - 1].setImage(new Image("file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\empty.png"));
                                        revEmptArea(finalX + 1, finalY - 1);
                                    }
                                    if (props[finalX + 1][finalY - 1] == coveredMine) {
                                        endGame(finalX + 1, finalY - 1);
                                    }
                                    if (props[finalX + 1][finalY - 1] == coveredNumber1 || props[finalX + 1][finalY - 1] == coveredNumber2 || props[finalX + 1][finalY - 1] == coveredNumber3 ||
                                            props[finalX + 1][finalY - 1] == coveredNumber4 || props[finalX + 1][finalY - 1] == coveredNumber5 || props[finalX + 1][finalY - 1] == coveredNumber6 ||
                                            props[finalX + 1][finalY - 1] == coveredNumber7 || props[finalX + 1][finalY - 1] == coveredNumber8) {
                                        opn(finalX + 1, finalY - 1, props[finalX + 1][finalY - 1]);
                                    }
                                }


                                if(finalX != 27) {
                                    if (props[finalX + 1][finalY] == coveredEmpty) {
                                        props[finalX + 1][finalY] = revealedEmpty;
                                        field[finalX + 1][finalY].setImage(new Image("file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\empty.png"));
                                        revEmptArea(finalX + 1, finalY);
                                    }
                                    if (props[finalX + 1][finalY] == coveredMine) {
                                        endGame(finalX + 1, finalY);
                                    }
                                    if (props[finalX + 1][finalY] == coveredNumber1 || props[finalX + 1][finalY] == coveredNumber2 || props[finalX + 1][finalY] == coveredNumber3 ||
                                            props[finalX + 1][finalY] == coveredNumber4 || props[finalX + 1][finalY] == coveredNumber5 || props[finalX + 1][finalY] == coveredNumber6 ||
                                            props[finalX + 1][finalY] == coveredNumber7 || props[finalX + 1][finalY] == coveredNumber8) {
                                        opn(finalX + 1, finalY, props[finalX + 1][finalY]);
                                    }
                                }


                                if(finalX != 27 && finalY != 19) {
                                    if (props[finalX + 1][finalY + 1] == coveredEmpty) {
                                        props[finalX + 1][finalY + 1] = revealedEmpty;
                                        field[finalX + 1][finalY + 1].setImage(new Image("file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\empty.png"));
                                        revEmptArea(finalX + 1, finalY + 1);
                                    }
                                    if (props[finalX + 1][finalY + 1] == coveredMine) {
                                        endGame(finalX + 1, finalY + 1);
                                    }
                                    if (props[finalX + 1][finalY + 1] == coveredNumber1 || props[finalX + 1][finalY + 1] == coveredNumber2 || props[finalX + 1][finalY + 1] == coveredNumber3 ||
                                            props[finalX + 1][finalY + 1] == coveredNumber4 || props[finalX + 1][finalY + 1] == coveredNumber5 || props[finalX + 1][finalY + 1] == coveredNumber6 ||
                                            props[finalX + 1][finalY + 1] == coveredNumber7 || props[finalX + 1][finalY + 1] == coveredNumber8) {
                                        opn(finalX + 1, finalY + 1, props[finalX + 1][finalY + 1]);
                                    }
                                }
                            }


                        //reveal single number spot
                        if(props[finalX][finalY] == coveredNumber1){
                            image.setImage(new Image(urlNum1));
                            props[finalX][finalY] = revealedNumber;
                            orgn[finalX][finalY] = 2;
                            checkOrgnFor2s();
                        }

                        if(props[finalX][finalY] == coveredNumber2){
                            image.setImage(new Image(urlNum2));
                            props[finalX][finalY] = revealedNumber;
                            orgn[finalX][finalY] = 2;
                            checkOrgnFor2s();
                        }

                        if(props[finalX][finalY] == coveredNumber3){
                            image.setImage(new Image(urlNum3));
                            props[finalX][finalY] = revealedNumber;
                            orgn[finalX][finalY] = 2;
                            checkOrgnFor2s();
                        }

                        if(props[finalX][finalY] == coveredNumber4){
                            image.setImage(new Image(urlNum4));
                            props[finalX][finalY] = revealedNumber;
                            orgn[finalX][finalY] = 2;
                            checkOrgnFor2s();
                        }

                        if(props[finalX][finalY] == coveredNumber5){
                            image.setImage(new Image(urlNum5));
                            props[finalX][finalY] = revealedNumber;
                            orgn[finalX][finalY] = 2;
                            checkOrgnFor2s();
                        }

                        if(props[finalX][finalY] == coveredNumber6){
                            image.setImage(new Image(urlNum6));
                            props[finalX][finalY] = revealedNumber;
                            orgn[finalX][finalY] = 2;
                            checkOrgnFor2s();
                        }

                        if(props[finalX][finalY] == coveredNumber7){
                            image.setImage(new Image(urlNum7));
                            props[finalX][finalY] = revealedNumber;
                            orgn[finalX][finalY] = 2;
                            checkOrgnFor2s();
                        }

                        if(props[finalX][finalY] == coveredNumber8){
                            image.setImage(new Image(urlNum8));
                            props[finalX][finalY] = revealedNumber;
                            orgn[finalX][finalY] = 2;
                            checkOrgnFor2s();
                        }

                        //reveal empty area
                        if(props[finalX][finalY] == coveredEmpty){
                            revEmptArea(finalX, finalY);
                            orgn[finalX][finalY] = 2;
                            checkOrgnFor2s();
                        }
                    }


                });

                field[x][y] = image;
            }
        }

        //set all to covered empty
        for(int y = 0; y < 20; y++) {
            for (int x = 0; x < 28; x++) {
                props[x][y] = coveredEmpty;
            }
        }

        //generate 90 randomly located mines
        while(numOfMines != 90) {
            int rx = (int) (Math.random() * 27);
            int ry = (int) (Math.random() * 19);
            if(props[rx][ry] != coveredMine) {
                props[rx][ry] = coveredMine;
                numOfMines++;
            }
        }

        //System.out.println(Arrays.deepToString(props));
        //populate orgn with 1s, 2s only
        for(int z = 0; z < props.length; z++){
            for(int m = 0; m < props[z].length; m++){
                orgn[z][m] = props[z][m];
            }
        }

        //put numbers around mines
        for(int y = 0; y < 20; y++) {
            for (int x = 0; x < 28; x++) {

                if(props[x][y] != coveredMine) {

                    if (x != 0 && y != 0) {
                        if (props[x - 1][y - 1] == coveredMine) {
                            cnt++;
                        }
                    }
                    if (x != 0) {
                        if (props[x - 1][y] == coveredMine) {
                            cnt++;
                        }
                    }
                    if (x != 0 && y != 19) {
                        if (props[x - 1][y + 1] == coveredMine) {
                            cnt++;
                        }
                    }
                    if (y != 0) {
                        if (props[x][y - 1] == coveredMine) {
                            cnt++;
                        }
                    }
                    if (y != 19) {
                        if (props[x][y + 1] == coveredMine) {
                            cnt++;
                        }
                    }
                    if (x != 27 && y != 0) {
                        if (props[x + 1][y - 1] == coveredMine) {
                            cnt++;
                        }
                    }
                    if (x != 27) {
                        if (props[x + 1][y] == coveredMine) {
                            cnt++;
                        }
                    }
                    if (x != 27 && y != 19) {
                        if (props[x + 1][y + 1] == coveredMine) {
                            cnt++;
                        }
                    }

                    if (cnt == 1) {
                        props[x][y] = coveredNumber1;
                        //field[x][y].setImage(new Image(urlNum1));
                    }
                    if (cnt == 2) {
                        props[x][y] = coveredNumber2;
                        //field[x][y].setImage(new Image(urlNum2));
                    }
                    if (cnt == 3) {
                        props[x][y] = coveredNumber3;
                        //field[x][y].setImage(new Image(urlNum3));
                    }
                    if (cnt == 4) {
                        props[x][y] = coveredNumber4;
                        //field[x][y].setImage(new Image(urlNum4));
                    }
                    if (cnt == 5) {
                        props[x][y] = coveredNumber5;
                       // field[x][y].setImage(new Image(urlNum5));
                    }
                    if (cnt == 6) {
                        props[x][y] = coveredNumber6;
                        //field[x][y].setImage(new Image(urlNum6));
                    }
                    if (cnt == 7) {
                        props[x][y] = coveredNumber7;
                        //field[x][y].setImage(new Image(urlNum7));
                    }
                    if (cnt == 8) {
                        props[x][y] = coveredNumber8;
                        //field[x][y].setImage(new Image(urlNum8));
                    }

                    cnt = 0;
                }
            }
        }

        //setting up scene & stage
        root.getChildren().add(title);
        root.getChildren().add(mines);
        root.getChildren().add(over);
        root.getChildren().add(numMines);
        root.getChildren().add(tm);
        primaryStage.setScene(new Scene(root, 875, 550));
        primaryStage.show();
    }

    //end game
    private void endGame(int x, int y){
        field[x][y].setImage(new Image("file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\mine.png"));
        over.setText("GAME OVER");
        for (int o = 0; y < 20; y++) {
            for (int p = 0; x < 28; x++) {
                field[o][p].setDisable(true);
            }
        }
        shakeStage(primaryStage);
    }

    private void checkOrgnFor2s(){
        boolean win = true;

        for (int[] ints : orgn) {
            for (int m = 0; m < ints.length; m++) {
                if (ints[m] != 2) {
                    win = false;
                    break;
                }
            }
        }
        if(win){fur();}
    }

    private void fur(){
        //win
        Timeline timeLine = new Timeline(new KeyFrame(new Duration(25), event -> root.setRotate(root.getRotate() + 5)));

        timeLine.setCycleCount(72);
        timeLine.play();

        TextArea win = new TextArea();
        win.setStyle("-fx-text-fill: red ;");
        win.setText("\n                       WIN !!!");
        win.setPrefHeight(60);
        win.setPrefWidth(200);
        win.setTranslateX(primaryStage.getWidth() / 2 - win.getPrefWidth() + 25);
        win.setTranslateY(primaryStage.getHeight() / 2 - win.getPrefHeight());
        win.setEditable(false);

        root.getChildren().add(win);

        String bip = "C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\win.mp3";
        Media hit = new Media(new File(bip).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.play();
    }

    //reveal #s
    private void revNums(int x, int y){
        if(props[x][y] == coveredEmpty){
            props[x][y] = revealedEmpty;
            field[x][y].setImage(new Image("file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\empty.png"));
            orgn[x][y] = 2;
            checkOrgnFor2s();
        }

        if(props[x][y] == coveredNumber1){
            props[x][y] = revealedNumber;
            field[x][y].setImage(new Image("file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\1.png"));
            orgn[x][y] = 2;
            checkOrgnFor2s();
        }

        if(props[x][y] == coveredNumber2){
            props[x][y] = revealedNumber;
            field[x][y].setImage(new Image("file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\2.png"));
            orgn[x][y] = 2;
            checkOrgnFor2s();
        }

        if(props[x][y] == coveredNumber3){
            props[x][y] = revealedNumber;
            field[x][y].setImage(new Image("file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\3.png"));
            orgn[x][y] = 2;
            checkOrgnFor2s();
        }

        if(props[x][y] == coveredNumber4){
            props[x][y] = revealedNumber;
            field[x][y].setImage(new Image("file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\4.png"));
            orgn[x][y] = 2;
            checkOrgnFor2s();
        }

        if(props[x][y] == coveredNumber5){
            props[x][y] = revealedNumber;
            field[x][y].setImage(new Image("file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\5.png"));
            orgn[x][y] = 2;
            checkOrgnFor2s();
        }

        if(props[x][y] == coveredNumber6){
            props[x][y] = revealedNumber;
            field[x][y].setImage(new Image("file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\6.png"));
            orgn[x][y] = 2;
            checkOrgnFor2s();
        }

        if(props[x][y] == coveredNumber7){
            props[x][y] = revealedNumber;
            field[x][y].setImage(new Image("file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\7.png"));
            orgn[x][y] = 2;
            checkOrgnFor2s();
        }

        if(props[x][y] == coveredNumber8){
            props[x][y] = revealedNumber;
            field[x][y].setImage(new Image("file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\8.png"));
            orgn[x][y] = 2;
            checkOrgnFor2s();
        }
    }

    private void opn(int s, int q, int stat){
        props[s][q] = revealedNumber;
        orgn[s][q] = 2;
        checkOrgnFor2s();

        if(stat == coveredNumber1){
            field[s][q].setImage(new Image(urlNum1));
        }
        if(stat == coveredNumber2){
            field[s][q].setImage(new Image(urlNum2));
        }
        if(stat == coveredNumber3){
            field[s][q].setImage(new Image(urlNum3));
        }
        if(stat == coveredNumber4){
            field[s][q].setImage(new Image(urlNum4));
        }
        if(stat == coveredNumber5){
            field[s][q].setImage(new Image(urlNum5));
        }
        if(stat == coveredNumber6){
            field[s][q].setImage(new Image(urlNum6));
        }
        if(stat == coveredNumber7){
            field[s][q].setImage(new Image(urlNum7));
        }
        if(stat == coveredNumber8){
            field[s][q].setImage(new Image(urlNum8));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    //empty area auto open
    private void revEmptArea(int uu, int pp){
        props[uu][pp] = revealedEmpty;
        field[uu][pp].setImage(new Image("file:C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\empty.png"));
        orgn[uu][pp] = 2;
        checkOrgnFor2s();

        for(int t = 0; t < 14; t++) {
            for (int y = 0; y < 20; y++) {
                for (int x = 0; x < 28; x++) {

                    if (props[x][y] == coveredEmpty || props[x][y] == coveredNumber1 || props[x][y] == coveredNumber2
                            || props[x][y] == coveredNumber3 || props[x][y] == coveredNumber4
                            || props[x][y] == coveredNumber5 || props[x][y] == coveredNumber6
                            || props[x][y] == coveredNumber7 || props[x][y] == coveredNumber8) {

                        if (x != 0 && y != 0) {
                            if (props[x - 1][y - 1] == revealedEmpty) {
                                revNums(x, y);
                            }
                        }
                        if (x != 0) {
                            if (props[x - 1][y] == revealedEmpty) {
                                revNums(x, y);
                            }
                        }
                        if (x != 0 && y != 19) {
                            if (props[x - 1][y + 1] == revealedEmpty) {
                                revNums(x, y);
                            }
                        }
                        if (y != 0) {
                            if (props[x][y - 1] == revealedEmpty) {
                                revNums(x, y);
                            }
                        }
                        if (y != 19) {
                            if (props[x][y + 1] == revealedEmpty) {
                                revNums(x, y);
                            }
                        }
                        if (x != 27 && y != 0) {
                            if (props[x + 1][y - 1] == revealedEmpty) {
                                revNums(x, y);
                            }
                        }
                        if (x != 27) {
                            if (props[x + 1][y] == revealedEmpty) {
                                revNums(x, y);
                            }
                        }
                        if (x != 27 && y != 19) {
                            if (props[x + 1][y + 1] == revealedEmpty) {
                                revNums(x, y);
                            }
                        }
                    }

                }
            }
        }
    }

    //shake stage when lose
    private void shakeStage(Stage primaryStage) {
        Timeline timelineX = new Timeline(new KeyFrame(Duration.seconds(0.1), t -> {
            if (xxx == 0) {
                primaryStage.setX(primaryStage.getX() + 10);
                xxx = 1;
            } else {
                primaryStage.setX(primaryStage.getX() - 10);
                xxx = 0;
            }
        }));

        timelineX.setCycleCount(4);
        timelineX.setAutoReverse(false);
        timelineX.play();


        Timeline timelineY = new Timeline(new KeyFrame(Duration.seconds(0.1), t -> {
            if (yyy == 0) {
                primaryStage.setY(primaryStage.getY() + 10);
                yyy = 1;
            } else {
                primaryStage.setY(primaryStage.getY() - 10);
                yyy = 0;
            }
        }));

        timelineY.setCycleCount(4);
        timelineY.setAutoReverse(false);
        timelineY.play();

            String bip = "C:\\Users\\basheer\\IdeaProjects\\minesweeper\\src\\orig\\Exp.mp3";
            Media hit = new Media(new File(bip).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(hit);
            mediaPlayer.play();
    }

}
