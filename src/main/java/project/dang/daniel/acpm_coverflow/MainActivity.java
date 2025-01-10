package project.dang.daniel.acpm_coverflow;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

public class MainActivity extends AppCompatActivity {
    ///////////////////////////////////////////////////////////////////////////////////////
    //1: Declare variables
    private FeatureCoverFlow mCoverFlow;
    private CoverFlowAdapter mAdapter;
    private ArrayList<Programme> programmesList;
    private TextSwitcher mTitle;

    //Popup "Home" menu - 1: Declare variables
    private ImageButton homeMenu;

    //Dialog to show up how_to_play.xml layout - 1: Declare variable
    private Dialog dialog;


    ///////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ///////////////////////////////////////////////////////////////////////////////////
        //2: Find reference and do casting
        mCoverFlow = (FeatureCoverFlow) findViewById(R.id.coverFlow);
        //Initialize programmesList
        programmesList = new ArrayList<>();
        //Set Adapter for mCoverFlow
        mAdapter = new CoverFlowAdapter(this, programmesList);
        mCoverFlow.setAdapter(mAdapter);
        //
        mTitle = (TextSwitcher) findViewById(R.id.title);
        mTitle.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                TextView textView = (TextView) inflater.inflate(R.layout.programme_title, null);
                return textView;
            }
        });


        //////////////////////////////////////////////////////////////////////////////////
        //3: Initialize few programmesList for testing - will be added below
        prepareProgrammesList();

        //////////////////////////////////////////////////////////////////////////////////
        //4: Setup programmes list
        mAdapter = new CoverFlowAdapter(this, programmesList);
        mCoverFlow.setAdapter(mAdapter);


        //////////////////////////////////////////////////////////////////////////////////
        //5: Set "Item Click" Listener for CoverFlow
        //Set click listener to detect when users click a programme item. When users click a programme item
        //Open up a Dialog View to display "programme details": name & image
        mCoverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //Pop up the name of selected programme
                //Toast.makeText(getApplicationContext(), "Click: " + programmesList.get(position).getProgramme_title(), Toast.LENGTH_SHORT).show();
                //Pop up a Dialog to display "programme details"
                //Dialog dialog = new Dialog(MainActivity.this);
                Dialog dialog = new Dialog(MainActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                //dialog.setContentView(R.layout.programme_dialog);
                dialog.setContentView(R.layout.programme_detail_dialog);
                dialog.setCanceledOnTouchOutside(true);
                //-------------------------------------------------------------------------

                //////
                //Programme title
                TextView selected_program = (TextView) dialog.findViewById(R.id.selected_program);
                selected_program.setText(programmesList.get(position).getProgramme_title());

                //Programme image
                ImageView programmeIllustrationImage = (ImageView) dialog.findViewById(R.id.selectedImage);
                programmeIllustrationImage.setImageResource(programmesList.get(position).getProgramme_image());

                //Other info will be extracted from all_programs_detail.xml
                TextView programDescription = (TextView) dialog.findViewById(R.id.programDescription);
                TextView programQualificationLevel = (TextView) dialog.findViewById(R.id.qualification_level);
                TextView programDuration = (TextView) dialog.findViewById(R.id.durationTxt);
                TextView programCareer = (TextView) dialog.findViewById(R.id.careerTxt);
                //Other "String" variables storing programme details
                String programTitle = "", description = "", qualificationLevel = "", duration = "", career = "";


                String selectedProgrammeTitle = getResources().getStringArray(R.array.all_programmes_array)[position];
                //4: Extract the relevant information from "all_programs_detail.xml" file by using XML parser
                try {
                    //5: Open program_detail.xml file stored in Assets folder
                    InputStream inputStream = getAssets().open("all_programs_detail.xml");

                    //6: Use of XML DOM Parser for extracting data
                    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance().newInstance();
                    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                    Document document = documentBuilder.parse(inputStream);
                    Element element = document.getDocumentElement();
                    element.normalize();

                    //7: Read all the nodes containing tag "program"
                    NodeList nodeList = document.getElementsByTagName("program");

                    //8: Loop through all nodes to find the relevant selected program
                    boolean program_found = false;
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        //9: Get "node" in xml file
                        Node node = nodeList.item(i);
                        Element sub_Element = (Element) node;
                        //9: Get the program "title"
                        programTitle = sub_Element.getElementsByTagName("title").item(0)
                                .getChildNodes().item(0).getNodeValue();
                        programDescription.setText(programTitle);
                        //10: Check if the program title is the selected program. If yes, display its detail
                        if (programTitle.contains(selectedProgrammeTitle)) {
                            description = sub_Element.getElementsByTagName("description").item(0)
                                    .getChildNodes().item(0).getNodeValue();
                            qualificationLevel = sub_Element.getElementsByTagName("qualification_level").item(0)
                                    .getChildNodes().item(0).getNodeValue();
                            duration = sub_Element.getElementsByTagName("duration").item(0)
                                    .getChildNodes().item(0).getNodeValue();
                            career = sub_Element.getElementsByTagName("career").item(0)
                                    .getChildNodes().item(0).getNodeValue();
                            //11: Change the variable program_found to "true"
                            program_found = true;
                        }
                    }

                    //11: If not found any program in the xml file, assign all variables to "Not found"
                    if (!program_found) {
                        description = "Not found";
                        qualificationLevel = "Not found";
                        duration = "Not found";
                        career = "Not found";
                    }

                    //12: Display the extracted program details into the TextView on Layout
                    programDescription.setText(description);
                    programQualificationLevel.setText(qualificationLevel);
                    programDuration.setText(duration);
                    programCareer.setText(career);

                } catch (Exception e) {
                    e.printStackTrace();
                }


                //----------------------------------------------------------------------
                //
                dialog.show();

                /*
                //Set position and size of dialog window
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();

                wlp.gravity = Gravity.TOP | Gravity.LEFT;
                //wlp.x = 100; //x position: horizontal
                //wlp.y = 550;//y position: vertical

                wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                window.setAttributes(wlp);
                */
            }
        });

        //Set "Scroll" listener for mCoverFlow, will be added below
        //mCoverFlow.setOnScrollPositionListener(onScrollListener());
        mCoverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            ///
            @Override
            public void onScrolledToPosition(int position) {
                //When the carousel has stopped at a specific position
                mTitle.setText(programmesList.get(position).getProgramme_title());
                //Toast.makeText(getApplicationContext(), "ProgrammeID= " + position, Toast.LENGTH_SHORT).show();
            }

            ///
            @Override
            public void onScrolling() {
                //When the carousel is scrolling
                mTitle.setText("");
            }
        });


        /////////////////////////////////////////////////////////////////////
        //Popup "Home" menu - 2: Find reference and set listener for homeMenu button
        homeMenu = (ImageButton) findViewById(R.id.home_menu);
        homeMenu.setVisibility(View.VISIBLE);
        homeMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Define Popup Menu for home_menu
                PopupMenu home_menu = new PopupMenu(MainActivity.this, homeMenu);
                //Populate home_menu with items define in "home_menu.xml" file
                home_menu.getMenuInflater().inflate(R.menu.home_menu, home_menu.getMenu());

                //Set on Menu item click listener for home_menu
                home_menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        //
                        Toast.makeText(getApplicationContext(), "Item clicked: " + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                        //
                        //Dialog to show about_ac.xml layout - 2:
                        //check which menu item has been clicked and then call according function to execute
                        if (menuItem.getTitle().toString().contains("About Animation College")) {
                            //Item "how to play" has been clicked
                            displayAboutAC();
                        }

                        return false;
                    }
                });

                //Show the home_menu
                home_menu.show();
            }
        });

    }


    ////////////////////////////////////////////////////////////////////////////////////////////
    //Dialog to show up about_ac.xml layout - 3: Add and implement displayAboutAC() function
    public void displayAboutAC() {
        //Display the about_ac.xml layout
        dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.about_ac);
        dialog.setCanceledOnTouchOutside(true);

        //Determine where to display how_to_play layout on screen:
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        //
        wlp.gravity = Gravity.LEFT | Gravity.TOP;
        wlp.x = 100; //x position: Here x position's value is pixels from left to right
        wlp.y = 100;// y position:  For y position value is from bottom to top.
        //
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        //Show dialog
        dialog.show();
    }






    /////////////////////////////////////////////////////////////////////////////////////
    //6: Add 12 programmes to programmesList
    private void prepareProgrammesList() {
        //Initialize programmesList
        programmesList = new ArrayList<>();

        //Add a programme to the "programmes list"
        Programme programme = new Programme(R.drawable.ames1_bcs, "BACHELOR OF CREATIVE SOFTWARE");
        programmesList.add(programme);

        //Add a programme to the "programmes list"
        programme = new Programme(R.drawable.ames2_webdev, "DIPLOMA IN WEB & APPLICATION DEVELOPMENT");
        programmesList.add(programme);

        //Add item
        programme = new Programme(R.drawable.ames3_networking, "DIPLOMA IN NETWORKING");
        programmesList.add(programme);

        //Add item
        programme = new Programme(R.drawable.ames4_cloud, "DIPLOMA IN CLOUD TECHNOLOGY");
        programmesList.add(programme);

        //Add item
        programme = new Programme(R.drawable.ames5_itessential, "NZ CERTIFICATE IN IT ESSENTIALS");
        programmesList.add(programme);

        //Add item
        programme = new Programme(R.drawable.ames6_itsupport, "CERTIFICATE IN INFORMATION TECHNOLOGY & CLIENT SUPPORT");
        programmesList.add(programme);

        //Add item
        programme = new Programme(R.drawable.ames7_sql, "CERTIFICATE IN SQL SERVER ADMINISTRATION & SUPPORT");
        programmesList.add(programme);

        //Add item
        programme = new Programme(R.drawable.animation1_boa, "BACHELOR OF ANIMATION");
        programmesList.add(programme);

        //Add item
        programme = new Programme(R.drawable.animation2_dia, "DIPLOMA IN ANIMATION");
        programmesList.add(programme);

        //Add item
        programme = new Programme(R.drawable.animation3_daa2d, "DIPLOMA IN APPLIED ANIMATION 2D");
        programmesList.add(programme);

        //Add item
        programme = new Programme(R.drawable.animation4_daa3d, "DIPLOMA IN APPLIED ANIMATION 3D");
        programmesList.add(programme);

        //Add item
        programme = new Programme(R.drawable.animation5_ddm, "DIPLOMA OF DIGITAL MEDIA (DDM)");
        programmesList.add(programme);
    }

}
