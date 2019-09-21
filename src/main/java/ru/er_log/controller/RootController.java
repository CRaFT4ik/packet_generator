package ru.er_log.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import ru.er_log.components.*;
import ru.er_log.model.MainSettingsModel;
import ru.er_log.ui.TextFieldExtended;

import static ru.er_log.components.Utils.log;

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
    @FXML private Button buttonHelp;
    @FXML private Button buttonResetFields;

    private MainSettingsModel mainSettingsModel;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        mainSettingsModel = new MainSettingsModel();

        comboboxNetadapters.setItems(mainSettingsModel.getNetworkInterfaces());

        comboboxIPv4tosPre.setItems(mainSettingsModel.getIpv4TosPreValues());
        comboboxIPv4tosPre.getSelectionModel().selectFirst();

        listPacket.setItems(mainSettingsModel.getConfigurationList());

        initializeBindings();
        initializeEventsListeners();
    }

    private void initializeBindings()
    {
        checkboxIPv4length.selectedProperty().addListener((obs, wasSelected, isNowSelected) ->
                fieldIPv4length.setDisable(!isNowSelected));
        checkboxIPv4checksum.selectedProperty().addListener((obs, wasSelected, isNowSelected) ->
                fieldIPv4checksum.setDisable(!isNowSelected));
        checkboxTCPchecksum.selectedProperty().addListener((obs, wasSelected, isNowSelected) ->
                fieldTCPchecksum.setDisable(!isNowSelected));
        checkboxUDPchecksum.selectedProperty().addListener((obs, wasSelected, isNowSelected) ->
                fieldUDPchecksum.setDisable(!isNowSelected));
        checkboxICMPchecksum.selectedProperty().addListener((obs, wasSelected, isNowSelected) ->
                fieldICMPchecksum.setDisable(!isNowSelected));
    }

    private void initializeEventsListeners()
    {
        listPacket.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                setCurrentViewConfig(listPacket.getSelectionModel().getSelectedItem());
            }
        });

        buttonListAdd.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                log("Adding current configuration to list...");

                eConfig current = generateCurrentViewConfig();
                mainSettingsModel.addConfigurationToList(current);
            }
        });

        buttonListRemove.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                eConfig selected = listPacket.getSelectionModel().getSelectedItem();
                if (selected != null)
                    mainSettingsModel.removeConfigurationFromList(selected);
            }
        });

        buttonListSave.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                Utils.serialize(
                        new ArrayList<eConfig>(mainSettingsModel.getConfigurationList()),
                        "export.epg"//_" + (new Random().nextInt(900000) + 100000) + ".epg"
                );
            }
        });

        buttonListLoad.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                Object object = Utils.deserialize("export.epg");
                if (object instanceof ArrayList<?> && ((ArrayList<?>) object).get(0) instanceof eConfig)
                {
                    ArrayList<eConfig> arrayList = (ArrayList<eConfig>) object;
                    for (eConfig eConfig : arrayList)
                        mainSettingsModel.addConfigurationToList(eConfig);
                }
            }
        });

        buttonHelp.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                mainSettingsModel.showHelpFile();
            }
        });

        buttonResetFields.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                // TODO: Create a default configuration and install it.
            }
        });
    }

    private void setCurrentViewConfig(eConfig config)
    {
        tabpaneNetworkLayer.getSelectionModel().select(config.getSelectedTabId());

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

            if (ipv4Config.getLength() != eConfig.AUTO_VALUE_INTEGER)
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

            fieldIPv4ttl.setText(Helper.numberToStringUIWrapper(ipv4Config.getTtl()));

            if (ipv4Config.getChecksum() != eConfig.AUTO_VALUE_INTEGER)
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

            if (tcpConfig.getChecksum() != eConfig.AUTO_VALUE_INTEGER)
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
            fieldUDPlength.setText(Helper.numberToStringUIWrapper(udpConfig.getLength()));

            if (udpConfig.getChecksum() != eConfig.AUTO_VALUE_INTEGER)
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

            if (icmpConfig.getChecksum() != eConfig.AUTO_VALUE_INTEGER)
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
                        : eConfig.AUTO_VALUE_INTEGER)
                .setId(Helper.stringToIntUIWrapper(fieldIPv4id))
                .setFlags(new eIPv4Config.flags(
                        checkboxIPv4flagsX.isSelected(),
                        checkboxIPv4flagsD.isSelected(),
                        checkboxIPv4flagsM.isSelected()))
                .setTtl(Helper.stringToShortUIWrapper(fieldIPv4ttl))
                .setChecksum(
                        (checkboxIPv4checksum.isSelected())
                        ? Helper.stringToIntUIWrapper(fieldIPv4checksum)
                        : eConfig.AUTO_VALUE_INTEGER)
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
                        : eConfig.AUTO_VALUE_INTEGER)
                .setUrgent(Helper.stringToIntUIWrapper(fieldTCPurgent))
                .setOptions(Helper.stringToLongUIWrapper(fieldTCPoptions))
                .setData(fieldTCPdata.getText())
                ;

        eUDPConfig udpConfig = new eUDPConfig()
                .setSrcPort(Helper.stringToIntUIWrapper(fieldUDPsrcport))
                .setDstPort(Helper.stringToIntUIWrapper(fieldUDPdstport))
                .setLength(Helper.stringToIntUIWrapper(fieldUDPlength))
                .setChecksum(
                        (checkboxUDPchecksum.isSelected())
                        ? Helper.stringToIntUIWrapper(fieldUDPchecksum)
                        : eConfig.AUTO_VALUE_INTEGER)
                .setData(fieldUDPdata.getText())
                ;

        eICMPConfig icmpConfig = new eICMPConfig()
                .setType(Helper.stringToShortUIWrapper(fieldICMPtype))
                .setCode(Helper.stringToShortUIWrapper(fieldICMPcode))
                .setChecksum(
                        (checkboxICMPchecksum.isSelected())
                        ? Helper.stringToIntUIWrapper(fieldICMPchecksum)
                        : eConfig.AUTO_VALUE_INTEGER)
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
                .setSelectedTabId(tabpaneNetworkLayer.getSelectionModel().getSelectedIndex())
                ;

        return mainConfig;
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
                textFieldExtended.setErrorStyle(true);
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
                textFieldExtended.setErrorStyle(true);
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
                textFieldExtended.setErrorStyle(true);
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
