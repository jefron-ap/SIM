    
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package gov.va.ohi.sim.client;

import gov.va.ohi.sim.client.event.handlers.UploadLegoEventHandler;
import gov.va.ohi.sim.client.event.handlers.UploadSmartEncounterEventHandler;
import gov.va.ohi.sim.client.event.handlers.UploadSmartEncounterToHdrEventHandler;
import gov.va.ohi.sim.client.event.handlers.UploadSmartEncounterToSimServerEventHandler;
import gov.va.ohi.sim.fx.activity.ActivityPanel;
import gov.va.ohi.sim.fx.arena.ConceptViewHelper;
import gov.va.ohi.sim.fx.component.view.table.AuthorCellValueFactory;
import gov.va.ohi.sim.fx.component.view.table.ModuleCellValueFactory;
import gov.va.ohi.sim.fx.component.view.table.PathCellValueFactory;
import gov.va.ohi.sim.fx.component.view.table.StatusCellValueFactory;
import gov.va.ohi.sim.fx.component.view.table.TimeCellValueFactory;
import gov.va.ohi.sim.fx.component.view.table.TypeCellValueFactory;
import gov.va.ohi.sim.fx.concept.GetConceptService;
import gov.va.ohi.sim.fx.description.view.table.DescriptionLanguageCellValueFactory;
import gov.va.ohi.sim.fx.description.view.table.DescriptionTextCellValueFactory;
import gov.va.ohi.sim.fx.relationship.view.table.RelationshipDestinationCellValueFactory;
import gov.va.ohi.sim.fx.taxonomy.SimTreeIcons;
import gov.va.ohi.sim.fx.taxonomy.SimTreeCell;
import gov.va.ohi.sim.fx.taxonomy.SimTreeItem;
import gov.va.ohi.sim.fx.taxonomy.TaxonomyProgressIndicatorSkin;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.util.Callback;
import org.ihtsdo.fxmodel.FxTaxonomyReferenceWithConcept;
import org.ihtsdo.fxmodel.concept.FxConcept;

/**
 *
 * @author kec
 */
public class Sample implements Initializable {
   private ObjectProperty<FxConcept> focusConcept      = new SimpleObjectProperty<>(this, "focusConcept",
                                                            App.fxc);
   private GetConceptService         getConceptService = new GetConceptService();
   @FXML
   private ProgressIndicator         conceptServiceProgress;
   @FXML
   private TableColumn               descriptionAuthorColumn;
   @FXML
   private TableColumn               descriptionLangColumn;
   @FXML
   private TableColumn               descriptionModuleColumn;
   @FXML
   private TableColumn               descriptionNidColumn;
   @FXML
   private TableColumn               descriptionPathColumn;
   @FXML
   private TableColumn               descriptionStatusColumn;
   @FXML
   private TableView                 descriptionTable;
   @FXML
   private TableColumn               descriptionTextColumn;
   @FXML
   private TableColumn               descriptionTimeColumn;
   @FXML
   private TableColumn               descriptionTypeColumn;
   @FXML
   private TableColumn               descriptionUuidColumn;
   @FXML
   private MenuItem                  loadLegoMenuItem;
   @FXML
   private ProgressBar               progressBar; 
   @FXML
   private MenuItem                  quitMenuItem;
   @FXML
   private TableColumn               relationshipAuthorColumn;
   @FXML
   private TableColumn               relationshipDestinationColumn;
   @FXML
   private TableColumn               relationshipModuleColumn;
   @FXML
   private TableColumn               relationshipNidColumn;
   @FXML
   private TableColumn               relationshipPathColumn;
   @FXML
   private TableColumn               relationshipStatusColumn;
   @FXML
   private TableView                 relationshipTable;
   @FXML
   private TableColumn               relationshipTimeColumn;
   @FXML
   private TableColumn               relationshipTypeColumn;
   @FXML
   private TableColumn               relationshipUuidColumn;
   @FXML
   private TreeView                  treeView;
   @FXML
   private MenuItem                  uploadSmartEncounterMenuItem;
   @FXML
   private MenuItem                  uploadSmartEncounterToHDRMenuItem;
   @FXML
   private MenuItem                  uploadSmartEncounterToSimServerMenuItem;
   @FXML
   private Region                    veil;
   @FXML
   private TreeView                  arenaConceptView;
   private ConceptViewHelper cvHelper;

   //~--- methods -------------------------------------------------------------

   @Override
   public void initialize(URL url, ResourceBundle rb) {
      focusConcept.bind(getConceptService.valueProperty());
      veil.visibleProperty().bind(getConceptService.runningProperty());
      conceptServiceProgress.visibleProperty().bind(getConceptService.runningProperty());
      conceptServiceProgress.progressProperty().bind(getConceptService.progressProperty());
      setupDescriptionTable();
      setupRelationshipTable();

      final InvalidationListener invalidationListener = new InvalidationListener() {
         @Override
         public void invalidated(Observable observable) {
            descriptionTable.itemsProperty().unbind();
            descriptionTable.setItems(null);
            relationshipTable.itemsProperty().unbind();
            relationshipTable.setItems(null);
         }
      };
      final ChangeListener changeListener = new ChangeListener() {
         @Override
         public void changed(ObservableValue ov, Object t, Object t1) {
            if ((t1 != null) && (t1 instanceof FxConcept)) {
               FxConcept fxc = (FxConcept) t1;

               descriptionTable.itemsProperty().bind(fxc.descriptions());
               relationshipTable.itemsProperty().bind(fxc.originRelationships());
            }
         }
      };

      focusConcept.addListener(invalidationListener);
      focusConcept.addListener(changeListener);
      descriptionTable.itemsProperty().bind(Bindings.select(focusConcept, "descriptions"));
      relationshipTable.itemsProperty().bind(Bindings.select(focusConcept, "originRelationships"));
      treeView.setCellFactory(new Callback<TreeView<FxTaxonomyReferenceWithConcept>,
              TreeCell<FxTaxonomyReferenceWithConcept>>() {
         @Override
         public TreeCell<FxTaxonomyReferenceWithConcept> call(TreeView<FxTaxonomyReferenceWithConcept> p) {
            return new SimTreeCell();
         }
      });
      treeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
      treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
         @Override
         public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            if (newValue instanceof SimTreeItem) {
               SimTreeItem simTreeItem = (SimTreeItem) newValue;

               getConceptService.setConceptUuid(simTreeItem.getValue().getConcept().getPrimordialUuid());
               getConceptService.restart();
            }
         }
      });
      treeView.setShowRoot(false);

      FxTaxonomyReferenceWithConcept root       = new FxTaxonomyReferenceWithConcept();
      SimTreeItem                    rootItem   = new SimTreeItem(root);
      FxTaxonomyReferenceWithConcept snomedRoot = new FxTaxonomyReferenceWithConcept();

      snomedRoot.setConcept(App.fxc);

      SimTreeItem item = new SimTreeItem(snomedRoot, SimTreeIcons.ROOT.getImageView());

      rootItem.getChildren().add(item);
      rootItem.setExpanded(true);

      // item.computeGraphic();
      item.addChildren();

      // put this event handler on the root
      item.addEventHandler(TreeItem.branchCollapsedEvent(), new EventHandler() {
         @Override
         public void handle(Event t) {

            // remove grandchildren
            SimTreeItem sourceTreeItem = (SimTreeItem) t.getSource();

            sourceTreeItem.removeGrandchildren();
         }
      });
      item.addEventHandler(TreeItem.branchExpandedEvent(), new EventHandler() {
         @Override
         public void handle(Event t) {

            // add grandchildren
            SimTreeItem       sourceTreeItem = (SimTreeItem) t.getSource();
            ProgressIndicator p2             = new ProgressIndicator();

            p2.setSkin(new TaxonomyProgressIndicatorSkin(p2));
            p2.setPrefSize(16, 16);
            p2.setProgress(-1);
            sourceTreeItem.setProgressIndicator(p2);
            sourceTreeItem.addChildrenConceptsAndGrandchildrenItems(p2);
         }
      });
      treeView.setRoot(rootItem);
      cvHelper = new ConceptViewHelper(arenaConceptView);
      quitMenuItem.setOnAction(new EventHandler<ActionEvent>() {
         @Override
         public void handle(ActionEvent t) {
            System.exit(0);
         }
      });
      uploadSmartEncounterMenuItem.setOnAction(new UploadSmartEncounterEventHandler(treeView));
      uploadSmartEncounterToHDRMenuItem.setOnAction(new UploadSmartEncounterToHdrEventHandler(treeView));
      uploadSmartEncounterToSimServerMenuItem.setOnAction(
          new UploadSmartEncounterToSimServerEventHandler(treeView));
      loadLegoMenuItem.setOnAction(new UploadLegoEventHandler(treeView, new ActivityPanel(progressBar)));
   }

   private void setupDescriptionTable() {
      descriptionUuidColumn.setCellValueFactory(new PropertyValueFactory("primordialComponentUuid"));
      descriptionNidColumn.setCellValueFactory(new PropertyValueFactory("componentNid"));
      descriptionTextColumn.setCellValueFactory(new DescriptionTextCellValueFactory());
      descriptionLangColumn.setCellValueFactory(new DescriptionLanguageCellValueFactory());
      descriptionTypeColumn.setCellValueFactory(new TypeCellValueFactory());
      descriptionStatusColumn.setCellValueFactory(new StatusCellValueFactory());
      descriptionTimeColumn.setCellValueFactory(new TimeCellValueFactory());
      descriptionAuthorColumn.setCellValueFactory(new AuthorCellValueFactory());
      descriptionModuleColumn.setCellValueFactory(new ModuleCellValueFactory());
      descriptionPathColumn.setCellValueFactory(new PathCellValueFactory());
   }

   private void setupRelationshipTable() {
      relationshipUuidColumn.setCellValueFactory(new PropertyValueFactory("primordialComponentUuid"));
      relationshipNidColumn.setCellValueFactory(new PropertyValueFactory("componentNid"));
      relationshipTypeColumn.setCellValueFactory(new TypeCellValueFactory());
      relationshipDestinationColumn.setCellValueFactory(new RelationshipDestinationCellValueFactory());
      relationshipStatusColumn.setCellValueFactory(new StatusCellValueFactory());
      relationshipTimeColumn.setCellValueFactory(new TimeCellValueFactory());
      relationshipAuthorColumn.setCellValueFactory(new AuthorCellValueFactory());
      relationshipModuleColumn.setCellValueFactory(new ModuleCellValueFactory());
      relationshipPathColumn.setCellValueFactory(new PathCellValueFactory());
   }
}
