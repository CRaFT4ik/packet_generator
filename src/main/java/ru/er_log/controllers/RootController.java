package ru.er_log.controllers;

import java.awt.event.FocusEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.ResourceBundle;

import com.sun.javafx.stage.FocusUngrabEvent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.pcap4j.util.ByteArrays;
import org.pcap4j.util.MacAddress;
import ru.er_log.utils.Log;
import ru.er_log.utils.ThreadUtils;
import ru.er_log.utils.Utils;
import ru.er_log.components.*;
import ru.er_log.models.MainSettingsModel;
import ru.er_log.ui.TextFieldExtended;

import static ru.er_log.utils.Log.out;
import static ru.er_log.utils.Log.err;

public class RootController implements Initializable
{
    @FXML private Button buttonETHsrcmacReset;
    @FXML private Button buttonETHdstmacReset;
    @FXML private TextFieldExtended fieldETHsrcmac;
    @FXML private TextFieldExtended fieldETHdstmac;

    @FXML private ComboBox<eIPv4Config.tos.precedence> comboboxIPv4tosPre;
    @FXML private CheckBox checkboxIPv4tosD;
    @FXML private CheckBox checkboxIPv4tosT;
    @FXML private CheckBox checkboxIPv4tosR;
    @FXML private CheckBox checkboxIPv4tosC;
    @FXML private CheckBox checkboxIPv4tosX;
    @FXML private CheckBox checkboxIPv4flagsX;
    @FXML private CheckBox checkboxIPv4flagsD;
    @FXML private CheckBox checkboxIPv4flagsM;
    @FXML private CheckBox checkboxIPv4length;
    @FXML private CheckBox checkboxIPv4checksum;
    @FXML private TextFieldExtended fieldIPv4id;
    @FXML private TextFieldExtended fieldIPv4ttl;
    @FXML private TextFieldExtended fieldIPv4srcip;
    @FXML private TextFieldExtended fieldIPv4dstip;
    @FXML private TextFieldExtended fieldIPv4options;
    @FXML private TextFieldExtended fieldIPv4ip;
    @FXML private TextFieldExtended fieldIPv4ihl;
    @FXML private TextFieldExtended fieldIPv4checksum;
    @FXML private TextFieldExtended fieldIPv4length;
    @FXML private TextFieldExtended fieldIPv4offset;

    @FXML private CheckBox checkboxTCPreserved_0;
    @FXML private CheckBox checkboxTCPreserved_1;
    @FXML private CheckBox checkboxTCPreserved_2;
    @FXML private CheckBox checkboxTCPreserved_3;
    @FXML private CheckBox checkboxTCPreserved_4;
    @FXML private CheckBox checkboxTCPreserved_5;
    @FXML private CheckBox checkboxTCPflagsU;
    @FXML private CheckBox checkboxTCPflagsA;
    @FXML private CheckBox checkboxTCPflagsP;
    @FXML private CheckBox checkboxTCPflagsR;
    @FXML private CheckBox checkboxTCPflagsS;
    @FXML private CheckBox checkboxTCPflagsF;
    @FXML private CheckBox checkboxTCPchecksum;
    @FXML private TextFieldExtended fieldTCPsrcport;
    @FXML private TextFieldExtended fieldTCPdstport;
    @FXML private TextFieldExtended fieldTCPseqnum;
    @FXML private TextFieldExtended fieldTCPacknum;
    @FXML private TextFieldExtended fieldTCPoffset;
    @FXML private TextFieldExtended fieldTCPwindowsize;
    @FXML private TextFieldExtended fieldTCPurgent;
    @FXML private TextFieldExtended fieldTCPoptions;
    @FXML private TextFieldExtended fieldTCPchecksum;
    @FXML private TextArea fieldTCPdata;

    @FXML private CheckBox checkboxUDPlength;
    @FXML private CheckBox checkboxUDPchecksum;
    @FXML private TextFieldExtended fieldUDPsrcport;
    @FXML private TextFieldExtended fieldUDPdstport;
    @FXML private TextFieldExtended fieldUDPlength;
    @FXML private TextFieldExtended fieldUDPchecksum;
    @FXML private TextArea fieldUDPdata;

    @FXML private CheckBox checkboxICMPchecksum;
    @FXML private TextFieldExtended fieldICMPtype;
    @FXML private TextFieldExtended fieldICMPcode;
    @FXML private TextFieldExtended fieldICMPid;
    @FXML private TextFieldExtended fieldICMPseq;
    @FXML private TextFieldExtended fieldICMPchecksum;
    @FXML private TextArea fieldICMPdata;

    @FXML private TabPane tabpaneNetworkLayer;
    @FXML private Slider sliderDelay;
    @FXML private ListView<eConfig> listPacket;
    @FXML private ComboBox<eNetworkInterface> comboboxNetadapters;
    @FXML private CheckBox checkboxLoop;
    @FXML private Button buttonStart;
    @FXML private Button buttonStop;
    @FXML private Button buttonListAdd;
    @FXML private Button buttonListRemove;
    @FXML private Button buttonListSave;
    @FXML private Button buttonListLoad;
    @FXML private Button buttonLog;
    @FXML private Button buttonHelp;
    @FXML private Button buttonResetFields;

    private Stage stage;
    private Stage logStage;
    private MainSettingsModel mainSettingsModel;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        mainSettingsModel = new MainSettingsModel();

        comboboxNetadapters.setItems(mainSettingsModel.getNetworkInterfaces());
        comboboxIPv4tosPre.setItems(mainSettingsModel.getIpv4TosPreValues());
        comboboxIPv4tosPre.getSelectionModel().selectFirst();

        listPacket.setItems(mainSettingsModel.getConfigurationList());

        eConfig config = mainSettingsModel.getDefaultConfig();
        setCurrentViewConfig(config);

        initializeBindings();
        initializeEventsListeners();
        initializeLogWindow();
    }

    public void setStage(Stage stage)
    {
        this.stage = stage;
    }

    private void initializeLogWindow()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/er_log/views/logs.fxml"));
            Parent root = loader.load();

            logStage = new Stage();
            LogsController controller = (LogsController) loader.getController();

            Scene logScene = new Scene(root);
            logStage.setResizable(true);
            logStage.setScene(logScene);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        Log.getInstance().attachLogErrObserver(new Log.ILogObserver()
        {
            @Override
            public void handleLog(String message)
            {
                if (!logStage.isShowing())
                    buttonLog.getStyleClass().add("error");
            }
        });
    }

    private void initializeBindings()
    {
        checkboxIPv4length.selectedProperty().addListener((obs, wasSelected, isNowSelected) ->
                fieldIPv4length.setDisable(!isNowSelected));
        checkboxIPv4checksum.selectedProperty().addListener((obs, wasSelected, isNowSelected) ->
                fieldIPv4checksum.setDisable(!isNowSelected));
        checkboxTCPchecksum.selectedProperty().addListener((obs, wasSelected, isNowSelected) ->
                fieldTCPchecksum.setDisable(!isNowSelected));
        checkboxUDPlength.selectedProperty().addListener((obs, wasSelected, isNowSelected) ->
                fieldUDPlength.setDisable(!isNowSelected));
        checkboxUDPchecksum.selectedProperty().addListener((obs, wasSelected, isNowSelected) ->
                fieldUDPchecksum.setDisable(!isNowSelected));
        checkboxICMPchecksum.selectedProperty().addListener((obs, wasSelected, isNowSelected) ->
                fieldICMPchecksum.setDisable(!isNowSelected));
    }

    private void initializeEventsListeners()
    {
        fieldIPv4srcip.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
            {
                if ((fieldIPv4srcip.getText() != null && !fieldIPv4srcip.getText().isEmpty())
                        && (fieldETHsrcmac.getText() == null || fieldETHsrcmac.getText().isEmpty()))
                    buttonETHsrcmacReset.fire();
            }
        });

        fieldIPv4dstip.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
            {
                if ((fieldIPv4dstip.getText() != null && !fieldIPv4dstip.getText().isEmpty())
                        && (fieldETHdstmac.getText() == null || fieldETHdstmac.getText().isEmpty()))
                    buttonETHdstmacReset.fire();
            }
        });

        buttonETHsrcmacReset.setOnAction(new EventHandler<ActionEvent>()
        {
            private void works()
            {
                eNetworkInterface eNetworkInterface = getCurrentNetworkInterface();
                if (eNetworkInterface == null)
                {
                    err("Network interface is not selected");
                    return;
                }

                String srcIP = fieldIPv4srcip.getText();
                if (srcIP.isEmpty())
                {
                    err("Source IP field is empty");
                    return;
                }

                MacAddress macAddress = Utils.getMacAddressByIp(eNetworkInterface, srcIP);
                if (macAddress == null) return;

                String mac = ByteArrays.toHexString(macAddress.getAddress(), "-").toUpperCase();
                if (!mac.isEmpty()) fieldETHsrcmac.setText(mac);
            }

            @Override
            public void handle(ActionEvent event)
            {
                new Thread(this::works).start();
            }
        });

        buttonETHdstmacReset.setOnAction(new EventHandler<ActionEvent>()
        {
            private void works()
            {
                eNetworkInterface eNetworkInterface = getCurrentNetworkInterface();
                if (eNetworkInterface == null)
                {
                    err("Network interface is not selected");
                    return;
                }

                String dstIP = fieldIPv4dstip.getText();
                if (dstIP.isEmpty())
                {
                    err("Destination IP field is empty");
                    return;
                }

                MacAddress macAddress = Utils.getMacAddressByIp(eNetworkInterface, dstIP);
                if (macAddress == null) return;

                String mac = ByteArrays.toHexString(macAddress.getAddress(), "-").toUpperCase();
                if (!mac.isEmpty()) fieldETHdstmac.setText(mac);
            }

            @Override
            public void handle(ActionEvent event)
            {
                new Thread(this::works).start();
            }
        });

        listPacket.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                setCurrentViewConfig(listPacket.getSelectionModel().getSelectedItem());
            }
        });

        buttonListAdd.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                out("Adding current configuration to list...");

                eConfig current = generateCurrentViewConfig();
                mainSettingsModel.addConfigurationToList(current);
            }
        });

        buttonListRemove.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                eConfig selected = listPacket.getSelectionModel().getSelectedItem();
                if (selected != null)
                    mainSettingsModel.removeConfigurationFromList(selected);
            }
        });

        buttonListSave.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle("Save Configuration File");
                File selectedDirectory = directoryChooser.showDialog(stage);
                if (selectedDirectory == null) return;

                Utils.serialize(
                        new ArrayList<eConfig>(mainSettingsModel.getConfigurationList()),
                        selectedDirectory.toString() + "/" + mainSettingsModel.getExportFilename()
                );
            }
        });

        buttonListLoad.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Configuration File");
                String fileFormat = "*." + mainSettingsModel.getExportFormat();
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter(stage.getTitle() + " files (" + fileFormat + ")", fileFormat));
                File selectedFile = fileChooser.showOpenDialog(stage);
                if (selectedFile == null) return;

                Object object = Utils.deserialize(selectedFile);
                if (object instanceof ArrayList<?> && ((ArrayList<?>) object).get(0) instanceof eConfig)
                {
                    ArrayList<eConfig> arrayList = (ArrayList<eConfig>) object;
                    for (eConfig eConfig : arrayList)
                        mainSettingsModel.addConfigurationToList(eConfig);
                }
            }
        });

        buttonLog.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                buttonLog.getStyleClass().removeAll(Collections.singleton("error"));

                logStage.setTitle(stage.getTitle() + " :: Log");
                logStage.show();
            }
        });

        buttonHelp.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                mainSettingsModel.showHelpFile();
            }
        });

        buttonResetFields.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                eConfig config = mainSettingsModel.getDefaultConfig();
                setCurrentViewConfig(config);
            }
        });

        buttonStart.setOnAction(new EventHandler<ActionEvent>()
        {
            private void works()
            {
                try
                {
                    eNetworkInterface networkInterface = getCurrentNetworkInterface();
                    if (networkInterface == null)
                    {
                        err("Network interface not selected");
                        return;
                    }

                    mainSettingsModel.startSending(networkInterface, new ArrayList<eConfig>(listPacket.getItems()), checkboxLoop.isSelected(), (long) sliderDelay.getValue());
                } catch (Exception e)
                {
                    err("Sending interrupted: ", e.getMessage());
                }
            }

            @Override
            public void handle(ActionEvent event)
            {
                ThreadUtils.Worker worker = ThreadUtils.getInstance().addTask(this::works, mainSettingsModel.hashCode());
                worker.work();
            }
        });

        buttonStop.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                ThreadUtils.Worker worker = ThreadUtils.getInstance().getTask(mainSettingsModel.hashCode());
                if (worker != null && !worker.isDone())
                    worker.cancel();
            }
        });
    }

    private eNetworkInterface getCurrentNetworkInterface()
    {
        return comboboxNetadapters.getSelectionModel().getSelectedItem();
    }

    private void setCurrentViewConfig(eConfig config)
    {
        if (config == null) return;

        tabpaneNetworkLayer.getSelectionModel().select(getTabIdByType(config.getSelectedType()));

        eEthernetConfig ethernetConfig = config.getEthernetConfig();
        if (ethernetConfig != null)
        {
            fieldETHsrcmac.setText(ethernetConfig.getSrcMAC());
            fieldETHdstmac.setText(ethernetConfig.getDstMAC());
        }

        eIPv4Config ipv4Config = config.getIpv4Config();
        if (ipv4Config != null)
        {
            fieldIPv4ip.setText(Helper.numberToStringUIWrapper(ipv4Config.getVersion()));
            fieldIPv4ihl.setText(Helper.numberToStringUIWrapper(ipv4Config.getIhl()));

            eIPv4Config.tos tos = ipv4Config.getTos();
            if (tos != null)
            {
                comboboxIPv4tosPre.setValue(tos.getPre());
                checkboxIPv4tosD.setSelected(tos.isD());
                checkboxIPv4tosT.setSelected(tos.isT());
                checkboxIPv4tosR.setSelected(tos.isR());
                checkboxIPv4tosC.setSelected(tos.isC());
                checkboxIPv4tosX.setSelected(tos.isX());
            }

            if (ipv4Config.getLength() != eConfig.AUTO_VALUE)
            {
                checkboxIPv4length.setSelected(true);
                fieldIPv4length.setText(Helper.numberToStringUIWrapper(ipv4Config.getLength()));
            } else
                checkboxIPv4length.setSelected(false);

            fieldIPv4id.setText(Helper.numberToStringUIWrapper(ipv4Config.getId()));

            eIPv4Config.flags flags = ipv4Config.getFlags();
            if (flags != null)
            {
                checkboxIPv4flagsX.setSelected(flags.isX());
                checkboxIPv4flagsD.setSelected(flags.isD());
                checkboxIPv4flagsM.setSelected(flags.isM());
            }

            fieldIPv4offset.setText(Helper.numberToStringUIWrapper(ipv4Config.getOffset()));
            fieldIPv4ttl.setText(Helper.numberToStringUIWrapper(ipv4Config.getTtl()));

            if (ipv4Config.getChecksum() != eConfig.AUTO_VALUE)
            {
                checkboxIPv4checksum.setSelected(true);
                fieldIPv4checksum.setText(Helper.numberToStringUIWrapper(ipv4Config.getChecksum()));
            } else
                checkboxIPv4checksum.setSelected(false);

            fieldIPv4srcip.setText(ipv4Config.getSrcIP());
            fieldIPv4dstip.setText(ipv4Config.getDstIP());
            fieldIPv4options.setText(Helper.numberToStringUIWrapper(ipv4Config.getOptions()));
        }

        eTCPConfig tcpConfig = config.getTcpConfig();
        if (tcpConfig != null)
        {
            fieldTCPsrcport.setText(Helper.numberToStringUIWrapper(tcpConfig.getSrcPort()));
            fieldTCPdstport.setText(Helper.numberToStringUIWrapper(tcpConfig.getDstPort()));
            fieldTCPseqnum.setText(Helper.numberToStringUIWrapper(tcpConfig.getSeqNum()));
            fieldTCPacknum.setText(Helper.numberToStringUIWrapper(tcpConfig.getAskNum()));
            fieldTCPoffset.setText(Helper.numberToStringUIWrapper(tcpConfig.getOffset()));

            eTCPConfig.reserved reserved = tcpConfig.getReserved();
            if (reserved != null)
            {
                checkboxTCPreserved_0.setSelected(reserved.isBit0());
                checkboxTCPreserved_1.setSelected(reserved.isBit1());
                checkboxTCPreserved_2.setSelected(reserved.isBit2());
                checkboxTCPreserved_3.setSelected(reserved.isBit3());
                checkboxTCPreserved_4.setSelected(reserved.isBit4());
                checkboxTCPreserved_5.setSelected(reserved.isBit5());
            }

            eTCPConfig.flags flags = tcpConfig.getFlags();
            if (flags != null)
            {
                checkboxTCPflagsU.setSelected(flags.isU());
                checkboxTCPflagsA.setSelected(flags.isA());
                checkboxTCPflagsP.setSelected(flags.isP());
                checkboxTCPflagsR.setSelected(flags.isR());
                checkboxTCPflagsS.setSelected(flags.isS());
                checkboxTCPflagsF.setSelected(flags.isF());
            }

            fieldTCPwindowsize.setText(Helper.numberToStringUIWrapper(tcpConfig.getWindowSize()));

            if (tcpConfig.getChecksum() != eConfig.AUTO_VALUE)
            {
                checkboxTCPchecksum.setSelected(true);
                fieldTCPchecksum.setText(Helper.numberToStringUIWrapper(tcpConfig.getChecksum()));
            } else
                checkboxTCPchecksum.setSelected(false);

            fieldTCPurgent.setText(Helper.numberToStringUIWrapper(tcpConfig.getUrgent()));
            fieldTCPoptions.setText(Helper.numberToStringUIWrapper(tcpConfig.getOptions()));
            fieldTCPdata.setText(tcpConfig.getData());
        }

        eUDPConfig udpConfig = config.getUdpConfig();
        if (udpConfig != null)
        {
            fieldUDPsrcport.setText(Helper.numberToStringUIWrapper(udpConfig.getSrcPort()));
            fieldUDPdstport.setText(Helper.numberToStringUIWrapper(udpConfig.getDstPort()));

            if (udpConfig.getLength() != eConfig.AUTO_VALUE)
            {
                checkboxUDPlength.setSelected(true);
                fieldUDPlength.setText(Helper.numberToStringUIWrapper(udpConfig.getLength()));
            } else
                checkboxUDPlength.setSelected(false);

            if (udpConfig.getChecksum() != eConfig.AUTO_VALUE)
            {
                checkboxUDPchecksum.setSelected(true);
                fieldUDPchecksum.setText(Helper.numberToStringUIWrapper(udpConfig.getChecksum()));
            } else
                checkboxUDPchecksum.setSelected(false);

            fieldUDPdata.setText(udpConfig.getData());
        }

        eICMPConfig icmpConfig = config.getIcmpConfig();
        if (icmpConfig != null)
        {
            fieldICMPtype.setText(Helper.numberToStringUIWrapper(icmpConfig.getType()));
            fieldICMPcode.setText(Helper.numberToStringUIWrapper(icmpConfig.getCode()));

            if (icmpConfig.getChecksum() != eConfig.AUTO_VALUE)
            {
                checkboxICMPchecksum.setSelected(true);
                fieldICMPchecksum.setText(Helper.numberToStringUIWrapper(icmpConfig.getChecksum()));
            } else
                checkboxICMPchecksum.setSelected(false);

            fieldICMPid.setText(Helper.numberToStringUIWrapper(icmpConfig.getId()));
            fieldICMPseq.setText(Helper.numberToStringUIWrapper(icmpConfig.getSeqNum()));
            fieldICMPdata.setText(icmpConfig.getData());
        }
    }

    private eConfig generateCurrentViewConfig()
    {
        eEthernetConfig ethernetConfig = new eEthernetConfig()
                .setSrcMAC(fieldETHsrcmac.getText())
                .setDstMAC(fieldETHdstmac.getText())
                ;

        eIPv4Config ipv4Config = new eIPv4Config()
                .setVersion(Helper.stringToShortUIWrapper(fieldIPv4ip))
                .setIhl(Helper.stringToShortUIWrapper(fieldIPv4ihl))
                .setTos(new eIPv4Config.tos(
                        comboboxIPv4tosPre.getValue(),
                        checkboxIPv4tosD.isSelected(),
                        checkboxIPv4tosT.isSelected(),
                        checkboxIPv4tosR.isSelected(),
                        checkboxIPv4tosC.isSelected(),
                        checkboxIPv4tosX.isSelected()))
                .setLength(
                        (checkboxIPv4length.isSelected())
                        ? Helper.stringToIntUIWrapper(fieldIPv4length)
                        : (int) eConfig.AUTO_VALUE)
                .setId(Helper.stringToIntUIWrapper(fieldIPv4id))
                .setFlags(new eIPv4Config.flags(
                        checkboxIPv4flagsX.isSelected(),
                        checkboxIPv4flagsD.isSelected(),
                        checkboxIPv4flagsM.isSelected()))
                .setOffset(Helper.stringToShortUIWrapper(fieldIPv4offset))
                .setTtl(Helper.stringToShortUIWrapper(fieldIPv4ttl))
                .setChecksum(
                        (checkboxIPv4checksum.isSelected())
                        ? Helper.stringToIntUIWrapper(fieldIPv4checksum)
                        : (int) eConfig.AUTO_VALUE)
                .setSrcIP(fieldIPv4srcip.getText())
                .setDstIP(fieldIPv4dstip.getText())
                .setOptions(Helper.stringToLongUIWrapper(fieldIPv4options))
                ;

        eTCPConfig tcpConfig = new eTCPConfig()
                .setSrcPort(Helper.stringToIntUIWrapper(fieldTCPsrcport))
                .setDstPort(Helper.stringToIntUIWrapper(fieldTCPdstport))
                .setSeqNum(Helper.stringToLongUIWrapper(fieldTCPseqnum))
                .setAskNum(Helper.stringToLongUIWrapper(fieldTCPacknum))
                .setOffset(Helper.stringToShortUIWrapper(fieldTCPoffset))
                .setReserved(new eTCPConfig.reserved(
                        checkboxTCPreserved_0.isSelected(),
                        checkboxTCPreserved_1.isSelected(),
                        checkboxTCPreserved_2.isSelected(),
                        checkboxTCPreserved_3.isSelected(),
                        checkboxTCPreserved_4.isSelected(),
                        checkboxTCPreserved_5.isSelected()))
                .setFlags(new eTCPConfig.flags(
                        checkboxTCPflagsU.isSelected(),
                        checkboxTCPflagsA.isSelected(),
                        checkboxTCPflagsP.isSelected(),
                        checkboxTCPflagsR.isSelected(),
                        checkboxTCPflagsS.isSelected(),
                        checkboxTCPflagsF.isSelected()))
                .setWindowSize(Helper.stringToIntUIWrapper(fieldTCPwindowsize))
                .setChecksum(
                        (checkboxTCPchecksum.isSelected())
                        ? Helper.stringToIntUIWrapper(fieldTCPchecksum)
                        : (int) eConfig.AUTO_VALUE)
                .setUrgent(Helper.stringToIntUIWrapper(fieldTCPurgent))
                .setOptions(Helper.stringToLongUIWrapper(fieldTCPoptions))
                .setData(fieldTCPdata.getText())
                ;

        eUDPConfig udpConfig = new eUDPConfig()
                .setSrcPort(Helper.stringToIntUIWrapper(fieldUDPsrcport))
                .setDstPort(Helper.stringToIntUIWrapper(fieldUDPdstport))
                .setLength(
                        (checkboxUDPlength.isSelected())
                        ? Helper.stringToIntUIWrapper(fieldUDPlength)
                        : (int) eConfig.AUTO_VALUE)
                .setChecksum(
                        (checkboxUDPchecksum.isSelected())
                        ? Helper.stringToIntUIWrapper(fieldUDPchecksum)
                        : (int) eConfig.AUTO_VALUE)
                .setData(fieldUDPdata.getText())
                ;

        eICMPConfig icmpConfig = new eICMPConfig()
                .setType(Helper.stringToShortUIWrapper(fieldICMPtype))
                .setCode(Helper.stringToShortUIWrapper(fieldICMPcode))
                .setChecksum(
                        (checkboxICMPchecksum.isSelected())
                        ? Helper.stringToIntUIWrapper(fieldICMPchecksum)
                        : (int) eConfig.AUTO_VALUE)
                .setId(Helper.stringToIntUIWrapper(fieldICMPid))
                .setSeqNum(Helper.stringToIntUIWrapper(fieldICMPseq))
                .setData(fieldICMPdata.getText())
                ;

        eConfig mainConfig = new eConfig()
                .setEthernetConfig(ethernetConfig)
                .setIpv4Config(ipv4Config)
                .setTcpConfig(tcpConfig)
                .setUdpConfig(udpConfig)
                .setIcmpConfig(icmpConfig)
                .setSelectedType(getTabTypeById(tabpaneNetworkLayer.getSelectionModel().getSelectedIndex()))
                ;

        return mainConfig;
    }

    private int getTabIdByType(eConfig.fourthLevelPackages type)
    {
        int selectedId = 0;
        switch (type)
        {
            case TCP:
                selectedId = 0;
                break;
            case UDP:
                selectedId = 1;
                break;
            case ICMP:
                selectedId = 2;
                break;
        }

        return selectedId;
    }

    private eConfig.fourthLevelPackages getTabTypeById(int tabId)
    {
        eConfig.fourthLevelPackages selectedType = eConfig.fourthLevelPackages.TCP;

        switch (tabId)
        {
            case 0:
                selectedType = eConfig.fourthLevelPackages.TCP;
                break;
            case 1:
                selectedType = eConfig.fourthLevelPackages.UDP;
                break;
            case 2:
                selectedType = eConfig.fourthLevelPackages.ICMP;
                break;
        }

        return selectedType;
    }

    private static class Helper
    {
        /* Converts String to numerical value if it possible.
         * If it can't, text field will be marked with error and return Integer.MIN_VALUE. */
        static int stringToIntUIWrapper(TextFieldExtended textFieldExtended)
        {
            try
            {
                return Integer.parseInt(textFieldExtended.getText());
            } catch (NumberFormatException nfe)
            {
//                textFieldExtended.setErrorStyle(true);
                return Integer.MIN_VALUE;
            }
        }

        /* Converts String to numerical value if it possible.
         * If it can't, text field will be marked with error and return Short.MIN_VALUE. */
        static short stringToShortUIWrapper(TextFieldExtended textFieldExtended)
        {
            try
            {
                return Short.parseShort(textFieldExtended.getText());
            } catch (NumberFormatException nfe)
            {
//                textFieldExtended.setErrorStyle(true);
                return Short.MIN_VALUE;
            }
        }

        /* Converts String to numerical value if it possible.
         * If it can't, text field will be marked with error and return Long.MIN_VALUE. */
        static long stringToLongUIWrapper(TextFieldExtended textFieldExtended)
        {
            try
            {
                return Long.parseLong(textFieldExtended.getText());
            } catch (NumberFormatException nfe)
            {
//                textFieldExtended.setErrorStyle(true);
                return Long.MIN_VALUE;
            }
        }

        static String numberToStringUIWrapper(int number)
        {
            if (number != Integer.MIN_VALUE)
                return String.valueOf(number);
            else
                return "";
        }

        static String numberToStringUIWrapper(short number)
        {
            if (number != Short.MIN_VALUE)
                return String.valueOf(number);
            else
                return "";
        }

        static String numberToStringUIWrapper(long number)
        {
            if (number != Long.MIN_VALUE)
                return String.valueOf(number);
            else
                return "";
        }
    }
}
