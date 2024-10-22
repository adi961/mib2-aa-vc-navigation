/*
 * Decompiled with CFR 0.152.
 */
package de.vw.mib.bap.mqbab2.audiosd.functions;

import de.vw.mib.bap.array.timer.Timer;
import de.vw.mib.bap.array.timer.TimerNotifier;
import de.vw.mib.bap.datatypes.BAPEntity;
import de.vw.mib.bap.functions.BAPFunctionListener;
import de.vw.mib.bap.functions.Property;
import de.vw.mib.bap.functions.PropertyListener;
import de.vw.mib.bap.mqbab2.audiosd.api.ASLAudioSDConstants;
import de.vw.mib.bap.mqbab2.common.api.configuration.ConfigurationServiceLanguageChangeListener;
import de.vw.mib.bap.mqbab2.common.api.exbox.ExboxService;
import de.vw.mib.bap.mqbab2.common.api.exbox.ExboxServiceListener;
import de.vw.mib.bap.mqbab2.common.api.exbox.datatypes.ExboxCurrentStationInfo;
import de.vw.mib.bap.mqbab2.common.api.media.MediaService;
import de.vw.mib.bap.mqbab2.common.api.media.MediaServiceListener;
import de.vw.mib.bap.mqbab2.common.api.media.datatypes.MediaCurrentTrackInfo;
import de.vw.mib.bap.mqbab2.common.api.navigation.NavigationService;
import de.vw.mib.bap.mqbab2.common.api.navigation.NavigationServiceListener;
import de.vw.mib.bap.mqbab2.common.api.radio.RadioService;
import de.vw.mib.bap.mqbab2.common.api.radio.RadioServiceListener;
import de.vw.mib.bap.mqbab2.common.api.radio.datatypes.RadioCurrentAMStationInfo;
import de.vw.mib.bap.mqbab2.common.api.radio.datatypes.RadioCurrentChannelInfo;
import de.vw.mib.bap.mqbab2.common.api.radio.datatypes.RadioCurrentDABStationInfo;
import de.vw.mib.bap.mqbab2.common.api.radio.datatypes.RadioCurrentFMStationInfo;
import de.vw.mib.bap.mqbab2.common.api.radio.datatypes.RadioCurrentSatRadioText;
import de.vw.mib.bap.mqbab2.common.api.smartphoneintegration.SmartphoneIntegrationService;
import de.vw.mib.bap.mqbab2.common.api.smartphoneintegration.SmartphoneIntegrationServiceListener;
import de.vw.mib.bap.mqbab2.common.api.sound.SoundService;
import de.vw.mib.bap.mqbab2.common.api.sound.SoundServiceListener;
import de.vw.mib.bap.mqbab2.common.api.stages.BAPStage;
import de.vw.mib.bap.mqbab2.common.api.stages.BAPStageInitializer;
import de.vw.mib.bap.mqbab2.common.api.stages.Function;
import de.vw.mib.bap.mqbab2.common.api.tvtuner.TvTunerService;
import de.vw.mib.bap.mqbab2.common.api.tvtuner.TvTunerServiceListener;
import de.vw.mib.bap.mqbab2.generated.audiosd.serializer.CurrentStationInfo_Status;

public class CurrentStationInfo
        extends Function
        implements TimerNotifier,
        Property,
        ASLAudioSDConstants,
        RadioServiceListener,
        MediaServiceListener,
        SoundServiceListener,
        TvTunerServiceListener,
        SmartphoneIntegrationServiceListener,
        NavigationServiceListener,
        ConfigurationServiceLanguageChangeListener,
        ExboxServiceListener {
    protected boolean transmitALongDABProgrammStation = false;
    protected boolean autostoreRunning = false;
    protected boolean manualTuning = false;
    private int currentAbsolutePosition = 0;
    private int lastAudioComponent = 1;
    private static /* final */ int TIMER_UPDATE_TIME;
    private Timer timer = null;
    private static /* final */ int TIMER_ACTION_NOTHING;
    private static /* final */ int TIMER_ACTION_UPDATE_DATA;
    private static /* final */ int TIMER_ACTION_STOP_IGNORE_ENTERTAINMENT_SUPRESSION;
    private static /* final */ int TIMER_UPDATE_SUPPRESSION_TIME;
    private Timer _suppressionTimer;
    private static /* final */ String MIRROR_LINK_NAME;
    private static /* final */ String APPLE_CAR_PLAY_NAME;
    private static /* final */ String GOOGLE_GAL_NAME;
    private static /* final */ String BAIDU_CARLIFE_NAME;
    private static /* final */ String TV_TUNER_SOURCE_AV_PI;
    private static /* final */ String I18N_FILTERCRITERIA_UNKNOWN_ARTIST_STRING;
    private static /* final */ String I18N_FILTERCRITERIA_UNKNOWN_ALBUM_STRING;
    private static /* final */ String I18N_FILTERCRITERIA_NOW_PLAYING_FOLDER_STRING;
    protected static /* final */ int[] RADIO_LISTENER_IDS;
    protected static /* final */ int[] MEDIA_LISTENER_IDS;
    protected static /* final */ int[] TV_TUNER_LISTENER_IDS;
    protected static /* final */ int[] SMARTPHONE_INTEGRATION_LISTENER_IDS;
    protected static /* final */ int[] NAVIGATION_LISTENER_IDS;
    protected static /* final */ int[] SOUND_LISTENER_IDS;
    protected static /* final */ int[] EXBOX_LISTENER_IDS;
    static /* synthetic */ Class class$de$vw$mib$bap$mqbab2$generated$audiosd$serializer$CurrentStationInfo_Status;
    public static CurrentStationInfo instance;
    public static String AndroidAutoTitle = "";
    public static String AndroidAutoArtist = "";
    public static String AndroidAutoAlbum = "";

    public static void refresh() {
        if (instance != null) {
            instance.process(-1);
        }
    }

    // @Override
    public BAPEntity init(BAPStageInitializer bAPStageInitializer) {
        instance = this;
        this.getRadioService().addRadioServiceListener(this, RADIO_LISTENER_IDS);
        this.getMediaService().addMediaServiceListener(this, MEDIA_LISTENER_IDS);
        this.getTvTunerService().addTvTunerServiceListener(this, TV_TUNER_LISTENER_IDS);
        this.getSmartphoneIntegrationService().addSmartphoneIntegrationServiceListener(this,
                SMARTPHONE_INTEGRATION_LISTENER_IDS);
        this.getNavigationService().addNavigationServiceListener(this, NAVIGATION_LISTENER_IDS);
        this.getSoundService().addSoundServiceListener(this, SOUND_LISTENER_IDS);
        this.getExboxService().addExboxServiceListener(this, EXBOX_LISTENER_IDS);
        this.getConfigurationService().addLanguageChangeListener(this);
        this.timer = this.getTimerService().createTimer(this, 200);
        this.lastAudioComponent = this.getSoundService().getCurrentAudioComponent();
        CurrentStationInfo_Status currentStationInfo_Status = this.dequeueBAPEntity();
        this.computeCurrentStationInfoStatus(currentStationInfo_Status);
        return currentStationInfo_Status;
    }

    // @Override
    public void setFunctionData(BAPStage bAPStage, Object object) {
        switch (bAPStage.getFunctionId()) {
            case 22: {
                this.setCurrentStationHandleSend((Integer) object);
                break;
            }
            case 43: {
                this.setTransmitLongDabPs((Boolean) object);
                break;
            }
            case 30: {
                this.setAutostoreRunning((Boolean) object);
                break;
            }
            case 20: {
                this.setManualTuneMode((Boolean) object);
                break;
            }
        }
    }

    protected void setCurrentStationInfoSend(Boolean bl) {
        int[] nArray = new int[] { 24, 20 };
        this.context.updateStages(this, nArray, bl);
    }

    protected CurrentStationInfo_Status dequeueBAPEntity() {
        return (CurrentStationInfo_Status) this.context.dequeueBAPEntity(this,
                class$de$vw$mib$bap$mqbab2$generated$audiosd$serializer$CurrentStationInfo_Status == null
                        ? (class$de$vw$mib$bap$mqbab2$generated$audiosd$serializer$CurrentStationInfo_Status = CurrentStationInfo
                                .class$("de.vw.mib.bap.mqbab2.generated.audiosd.serializer.CurrentStationInfo_Status"))
                        : class$de$vw$mib$bap$mqbab2$generated$audiosd$serializer$CurrentStationInfo_Status);
    }

    // @Override
    public int getFunctionId() {
        return 21;
    }

    private Timer getSuppressionTimer() {
        if (this._suppressionTimer == null) {
            this._suppressionTimer = this.getTimerService().createTimer(this, 2000);
        }
        return this._suppressionTimer;
    }

    private void setArtistAndAlbum(CurrentStationInfo_Status currentStationInfo_Status) {
        String string = this.getMediaService().getCurrentTrackInfo().getCurrentTrackArtist();
        if (string != null && string.compareTo("filterCriteria.unknownArtist") == 0) {
            currentStationInfo_Status.secondaryInformation.setNullString();
            currentStationInfo_Status.si_Type = 73;
        } else {
            currentStationInfo_Status.secondaryInformation.setContent(string);
            currentStationInfo_Status.si_Type = string != null && string.length() != 0 ? 73 : 0;
        }
        String string2 = this.getMediaService().getCurrentTrackInfo().getCurrentTrackAlbum();
        if (string2 != null && string2.compareTo("filterCriteria.unknownAlbum") == 0) {
            currentStationInfo_Status.tertiaryInformation.setNullString();
            currentStationInfo_Status.ti_Type = 74;
        } else {
            currentStationInfo_Status.tertiaryInformation.setContent(string2);
            currentStationInfo_Status.ti_Type = string2 != null && string2.length() != 0 ? 74 : 0;
        }
    }

    protected /* final */ boolean isRadioTextPlusEnabledAMFM() {
        RadioService radioService = this.getRadioService();
        return radioService.isRadioTextSetupActive() && radioService.isRadioTextPlusSetupActive()
                && radioService.isRatioTextPlusActive() && radioService.isRDSSetupOptionEnabled();
    }

    private boolean isRadioTextPlusEnabledDAB() {
        RadioService radioService = this.getRadioService();
        return radioService.isRadioTextPlusSetupActive() && radioService.getDABRadioTextPlusActive();
    }

    private void setStationInfoNoInformationAvailable(CurrentStationInfo_Status currentStationInfo_Status) {
        currentStationInfo_Status.primaryInformation.setContent("");
        currentStationInfo_Status.pi_Type = 0;
        currentStationInfo_Status.pi_Id = 0;
        currentStationInfo_Status.secondaryInformation.setContent("");
        currentStationInfo_Status.si_Type = 0;
        currentStationInfo_Status.tertiaryInformation.setContent("");
        currentStationInfo_Status.ti_Type = 0;
        currentStationInfo_Status.quaternaryInformation.setContent("");
        currentStationInfo_Status.qi_Type = 0;
    }

    protected void setStationInfoForMediaDigitalAudioCD(CurrentStationInfo_Status currentStationInfo_Status) {
        boolean bl;
        MediaCurrentTrackInfo mediaCurrentTrackInfo = this.getMediaService().getCurrentTrackInfo();
        String string = mediaCurrentTrackInfo.getCurrentTrackTitle();
        boolean bl2 = bl = string != null && string.trim().length() != 0;
        if (bl) {
            currentStationInfo_Status.primaryInformation.setContent(string);
            currentStationInfo_Status.pi_Type = 72;
            currentStationInfo_Status.pi_Id = 0;
            this.setArtistAndAlbum(currentStationInfo_Status);
            currentStationInfo_Status.quaternaryInformation.setContent("");
            currentStationInfo_Status.qi_Type = 0;
        } else {
            int n = mediaCurrentTrackInfo.getCurrentTrackNumber();
            currentStationInfo_Status.primaryInformation.setContent("");
            currentStationInfo_Status.pi_Type = n != 0 ? 8 : 0;
            currentStationInfo_Status.pi_Id = n;
            currentStationInfo_Status.secondaryInformation.setContent("");
            currentStationInfo_Status.si_Type = 0;
            currentStationInfo_Status.tertiaryInformation.setContent("");
            currentStationInfo_Status.ti_Type = currentStationInfo_Status.si_Type;
            currentStationInfo_Status.quaternaryInformation.setContent("");
            currentStationInfo_Status.qi_Type = currentStationInfo_Status.ti_Type;
        }
    }

    private boolean isOnlyFilenameAvailable() {
        MediaCurrentTrackInfo mediaCurrentTrackInfo = this.getMediaService().getCurrentTrackInfo();
        return mediaCurrentTrackInfo.getCurrentTrackTitle()
                .compareTo(mediaCurrentTrackInfo.getCurrentTrackFilename()) == 0
                && mediaCurrentTrackInfo.getCurrentTrackAlbum().length() == 0
                && mediaCurrentTrackInfo.getCurrentTrackArtist().length() == 0;
    }

    private String getActivePlayingFolderName() {
        String string = this.getMediaService().getCurrentlyPlayedFolderName();
        return string.compareTo("filterCriteria.nowPlaying") == 0 ? "" : string;
    }

    protected void setStationInfoForMediaExternalDevice(CurrentStationInfo_Status currentStationInfo_Status) {
        MediaCurrentTrackInfo mediaCurrentTrackInfo = this.getMediaService().getCurrentTrackInfo();
        String string = mediaCurrentTrackInfo.getCurrentTrackTitle();
        boolean bl = string.trim().length() != 0;
        int n = mediaCurrentTrackInfo.getCurrentTrackNumber();
        if (bl || n == 0) {
            currentStationInfo_Status.primaryInformation.setContent(string);
            currentStationInfo_Status.pi_Type = bl ? (this.isOnlyFilenameAvailable() ? 6 : 72) : 0;
            currentStationInfo_Status.pi_Id = 0;
            this.setArtistAndAlbum(currentStationInfo_Status);
            String string2 = currentStationInfo_Status.pi_Type != 0 || currentStationInfo_Status.si_Type != 0
                    || currentStationInfo_Status.ti_Type != 0 ? this.getActivePlayingFolderName() : "";
            currentStationInfo_Status.quaternaryInformation.setContent(string2);
            int n2 = currentStationInfo_Status.qi_Type = string2.length() != 0 ? 1 : 0;
            if (string2.length() > 0 && currentStationInfo_Status.pi_Type == 0) {
                currentStationInfo_Status.pi_Type = 72;
            }
        } else {
            currentStationInfo_Status.primaryInformation.setContent("");
            currentStationInfo_Status.pi_Type = n != 0 ? 6 : 0;
            currentStationInfo_Status.pi_Id = n;
            currentStationInfo_Status.secondaryInformation.setContent("");
            currentStationInfo_Status.si_Type = 0;
            currentStationInfo_Status.tertiaryInformation.setContent("");
            currentStationInfo_Status.ti_Type = 0;
            String string3 = this.getActivePlayingFolderName();
            currentStationInfo_Status.quaternaryInformation.setContent(string3);
            currentStationInfo_Status.qi_Type = string3.length() != 0 ? 1 : 0;
        }
    }

    private void setStationInfoForMedia(CurrentStationInfo_Status currentStationInfo_Status) {
        MediaService mediaService = this.getMediaService();
        block0: switch (mediaService.getCurrentAudioSource()) {
            case 1:
            case 4: {
                switch (mediaService.getCDContentType()) {
                    case 3:
                    case 4: {
                        int n = mediaService.getDvdChapter();
                        int n2 = mediaService.getDvdChapterCount();
                        if (n == 0 || n2 == 0) {
                            currentStationInfo_Status.primaryInformation.setContent("");
                            currentStationInfo_Status.pi_Type = 0;
                            currentStationInfo_Status.pi_Id = 0;
                        } else {
                            currentStationInfo_Status.primaryInformation
                                    .setContent(new StringBuffer().append(n).append("/").append(n2).toString());
                            currentStationInfo_Status.pi_Type = 65;
                            currentStationInfo_Status.pi_Id = n;
                        }
                        currentStationInfo_Status.secondaryInformation.setContent("");
                        currentStationInfo_Status.si_Type = 0;
                        currentStationInfo_Status.tertiaryInformation.setContent("");
                        currentStationInfo_Status.ti_Type = currentStationInfo_Status.si_Type;
                        currentStationInfo_Status.quaternaryInformation.setContent("");
                        currentStationInfo_Status.qi_Type = currentStationInfo_Status.ti_Type;
                        break block0;
                    }
                    case 1: {
                        this.setStationInfoForMediaDigitalAudioCD(currentStationInfo_Status);
                        break block0;
                    }
                    case 2: {
                        this.setStationInfoForMediaExternalDevice(currentStationInfo_Status);
                        break block0;
                    }
                }
                this.setStationInfoNoInformationAvailable(currentStationInfo_Status);
                break;
            }
            case 7:
            case 14: {
                this.setStationInfoForMediaExternalDevice(currentStationInfo_Status);
                currentStationInfo_Status.quaternaryInformation.setContent("");
                currentStationInfo_Status.qi_Type = 0;
                break;
            }
            case 2:
            case 5:
            case 6:
            case 8:
            case 10:
            case 11:
            case 13: {
                this.setStationInfoForMediaExternalDevice(currentStationInfo_Status);
                break;
            }
            case 3:
            case 9: {
                this.setStationInfoNoInformationAvailable(currentStationInfo_Status);
                break;
            }
            default: {
                this.setStationInfoNoInformationAvailable(currentStationInfo_Status);
            }
        }
    }

    private void setStationInfoForMirrorLink(CurrentStationInfo_Status currentStationInfo_Status) {
        int n;
        String string;


        switch (this.getSmartphoneIntegrationService().getSmarphoneIntegrationActiveConnectionType()) {
            case 1: {
                string = "MirrorLink\u2122";
                n = 72;
                break;
            }
            case 2: {
                string = "Apple CarPlay";
                n = 72;
                break;
            }
            case 3: { // Android Auto
                this.setStationInfoForMedia(currentStationInfo_Status);
                return;
            }
            case 4: {
                string = "Baidu CarLife";
                n = 72;
                break;
            }
            default: {
                string = "";
                n = 0;
            }
        }
        currentStationInfo_Status.primaryInformation.setContent(string);
        currentStationInfo_Status.pi_Type = n;

    }

    private void setStationInfoForExbox(CurrentStationInfo_Status currentStationInfo_Status) {
        ExboxCurrentStationInfo exboxCurrentStationInfo = this.getExboxService().getExboxCurrentStationInfo();
        currentStationInfo_Status.primaryInformation.setContent(exboxCurrentStationInfo.getPrimaryInfo());
        currentStationInfo_Status.pi_Type = exboxCurrentStationInfo.getPrimaryInfoType();
        currentStationInfo_Status.pi_Id = exboxCurrentStationInfo.getPrimaryID();
        currentStationInfo_Status.secondaryInformation.setContent(exboxCurrentStationInfo.getSecondaryInfo());
        currentStationInfo_Status.si_Type = exboxCurrentStationInfo.getSecondaryInfoType();
        currentStationInfo_Status.tertiaryInformation.setContent(exboxCurrentStationInfo.getTertiaryInfo());
        currentStationInfo_Status.ti_Type = exboxCurrentStationInfo.getTertiaryInfoType();
        currentStationInfo_Status.quaternaryInformation.setContent(exboxCurrentStationInfo.getQuaternaryInfo());
        currentStationInfo_Status.qi_Type = exboxCurrentStationInfo.getPrimaryInfoType();
    }

    protected boolean isHDRadioAvailable() {
        return false;
    }

    protected void setStationInfoForRadioAM(CurrentStationInfo_Status currentStationInfo_Status) {
        String string;
        String string2;
        RadioService radioService = this.getRadioService();
        RadioCurrentAMStationInfo radioCurrentAMStationInfo = radioService.getCurrentAMStationInfo();
        String string3 = radioCurrentAMStationInfo.getCurrentAMStationInfoStationName();
        String string4 = this.formatFrequency(radioCurrentAMStationInfo.getCurrentAMStationInfoFrequency(), 1);
        if (string3.length() != 0 && !this.manualTuning) {
            currentStationInfo_Status.primaryInformation.setContent(string3);
        } else {
            currentStationInfo_Status.primaryInformation.setContent(string4);
        }
        boolean bl = radioService.isRadioSeekActive() || this.autostoreRunning;
        currentStationInfo_Status.pi_Type = bl ? 0 : 68;
        currentStationInfo_Status.pi_Id = 0;
        if (this.manualTuning && string3.length() != 0) {
            currentStationInfo_Status.secondaryInformation.setContent(string3);
            currentStationInfo_Status.si_Type = bl ? 0 : 68;
        } else {
            currentStationInfo_Status.secondaryInformation.setContent("");
            currentStationInfo_Status.si_Type = 0;
        }
        if (!this.manualTuning && this.isRadioTextPlusEnabledAMFM()) {
            string2 = radioService.getCurrentAMRadioTextTitleName();
            string = radioService.getCurrentAMRadioTextArtistName();
        } else {
            string2 = "";
            string = "";
        }
        currentStationInfo_Status.tertiaryInformation.setContent(string2);
        currentStationInfo_Status.ti_Type = string2.length() != 0 ? 72 : 0;
        currentStationInfo_Status.quaternaryInformation.setContent(string);
        currentStationInfo_Status.qi_Type = string.length() != 0 ? 73 : 0;
    }

    protected void setStationInfoForRadioFM(CurrentStationInfo_Status currentStationInfo_Status) {
        String string;
        String string2;
        boolean bl;
        RadioService radioService = this.getRadioService();
        RadioCurrentFMStationInfo radioCurrentFMStationInfo = radioService.getCurrentFMStationInfo();
        String string3 = radioCurrentFMStationInfo.getCurrentFMStationInfoStationName();
        String string4 = this.formatFrequency(radioCurrentFMStationInfo.getCurrentFMStationInfoFrequency(), 0);
        boolean bl2 = bl = !this.manualTuning && radioService.isPiCodingActivated();
        if (string3.length() != 0 && bl) {
            currentStationInfo_Status.primaryInformation.setContent(string3);
        } else {
            currentStationInfo_Status.primaryInformation.setContent(string4);
        }
        boolean bl3 = this.autostoreRunning || !bl;
        currentStationInfo_Status.pi_Type = bl3 ? 0 : 68;
        currentStationInfo_Status.pi_Id = 0;
        if (bl) {
            currentStationInfo_Status.secondaryInformation.setContent(string4);
            currentStationInfo_Status.si_Type = bl3 ? 0 : 67;
        } else {
            currentStationInfo_Status.secondaryInformation.setContent(string3);
            int n = currentStationInfo_Status.si_Type = string3.length() != 0 && !bl3 ? 68 : 0;
        }
        if (!this.manualTuning && this.isRadioTextPlusEnabledAMFM()) {
            string2 = radioService.getCurrentFMRadioTextTitleName();
            string = radioService.getCurrentFMRadioTextArtistName();
        } else {
            string2 = "";
            string = "";
        }
        currentStationInfo_Status.tertiaryInformation.setContent(string2);
        currentStationInfo_Status.ti_Type = string2.length() != 0 ? 72 : 0;
        currentStationInfo_Status.quaternaryInformation.setContent(string);
        currentStationInfo_Status.qi_Type = string.length() != 0 ? 73 : 0;
    }

    protected void setStationProperties(CurrentStationInfo_Status currentStationInfo_Status) {
        RadioService radioService = this.getRadioService();
        currentStationInfo_Status.stationProperties.dabServiceLinkedToFm = this.getSoundService()
                .getCurrentAudioComponent() == 1 && radioService.getCurrentStationBand() == 2
                        ? radioService.getDABServiceState() == 3
                        : false;
        currentStationInfo_Status.stationProperties.ibocLiveTransmissionActiveHdLiveMode = false;
    }

    protected void setStationInfoForRadioDAB(CurrentStationInfo_Status currentStationInfo_Status) {
        int n;
        String string;
        RadioService radioService = this.getRadioService();
        RadioCurrentDABStationInfo radioCurrentDABStationInfo = radioService.getCurrentDABStationInfo();
        boolean bl = false;
        int n2 = radioService.getDABServiceState();
        int n3 = radioService.getDABEnsembleState();
        int n4 = radioService.getDABAdditionalServiceState();
        if (n3 == 1) {
            string = radioCurrentDABStationInfo.getCurrentDABStationFrequencyLabel();
            n = 70;
            bl = true;
        } else if (n3 == 2) {
            string = radioCurrentDABStationInfo.getCurrentDABStationFrequencyLabel();
            n = 0;
            bl = true;
        } else if (n2 == 1) {
            string = !this.transmitALongDABProgrammStation
                    ? radioCurrentDABStationInfo.getCurrentDABStationServiceShortName()
                    : radioCurrentDABStationInfo.getCurrentDABStationServiceProgrammStation();
            n = 70;
        } else if (n4 == 2) {
            string = !this.transmitALongDABProgrammStation
                    ? radioCurrentDABStationInfo.getCurrentDABStationSecondaryServiceShortName()
                    : radioCurrentDABStationInfo.getCurrentDABStationSecondaryServiceName();
            n = 76;
        } else {
            string = !this.transmitALongDABProgrammStation
                    ? radioCurrentDABStationInfo.getCurrentDABStationServiceShortName()
                    : radioCurrentDABStationInfo.getCurrentDABStationServiceProgrammStation();
            n = 70;
        }
        if ((!this.manualTuning || bl) && string.length() > 0) {
            currentStationInfo_Status.primaryInformation.setContent(string);
            currentStationInfo_Status.pi_Type = n;
        } else {
            currentStationInfo_Status.primaryInformation
                    .setContent(radioCurrentDABStationInfo.getCurrentDABStationFrequencyLabel());
            currentStationInfo_Status.pi_Type = 0;
        }
        currentStationInfo_Status.pi_Id = this.currentAbsolutePosition;
        currentStationInfo_Status.secondaryInformation
                .setContent(radioCurrentDABStationInfo.getCurrentDABStationEnsembleName());
        currentStationInfo_Status.si_Type = currentStationInfo_Status.secondaryInformation.isEmptyString() ? 0 : 75;
        String string2 = "";
        String string3 = "";
        if (!this.manualTuning && this.isRadioTextPlusEnabledDAB()) {
            string2 = radioService.getCurrentDABRadioTextPlusTitleName();
            string3 = radioService.getCurrentDABRadioTextPlusArtistName();
        } else {
            string2 = "";
            string3 = "";
        }
        if (!this.manualTuning || bl) {
            currentStationInfo_Status.tertiaryInformation.setContent(string2);
            currentStationInfo_Status.ti_Type = string2.length() != 0 ? 72 : 0;
        } else {
            currentStationInfo_Status.tertiaryInformation.setContent(string);
            currentStationInfo_Status.ti_Type = n;
        }
        currentStationInfo_Status.quaternaryInformation.setContent(string3);
        currentStationInfo_Status.qi_Type = string3.length() != 0 ? 73 : 0;
    }

    protected String formatFrequency(int n, int n2) {
        String string = this.getRadioService().getFMFrequencyScale() == 1
                ? this.getFixFormatter().fmtIcFrequencyNAR(n, n2, -1)
                : this.getFixFormatter().fmtIcFrequency(n, n2, this.getConfigurationService().getCurrentGUILanguage());
        return string;
    }

    private void setStationInfoForRadioSirius(CurrentStationInfo_Status currentStationInfo_Status) {
        RadioService radioService = this.getRadioService();
        RadioCurrentChannelInfo radioCurrentChannelInfo = radioService.getCurrentChannelInfo();
        RadioCurrentSatRadioText radioCurrentSatRadioText = radioService.getCurrentSatRadioText();
        currentStationInfo_Status.channel_Id = radioCurrentChannelInfo.getCurrentSatChannelNumber();
        currentStationInfo_Status.primaryInformation
                .setContent(this.transmitALongDABProgrammStation ? radioCurrentChannelInfo.getCurrentSatChannelName()
                        : radioCurrentChannelInfo.getCurrentSatChannelShortName());
        currentStationInfo_Status.pi_Type = 69;
        String string = radioCurrentSatRadioText.getCurrentSatRadioTextTitleName();
        currentStationInfo_Status.secondaryInformation.setContent(string);
        currentStationInfo_Status.si_Type = string.length() != 0 ? 72 : 0;
        String string2 = radioCurrentSatRadioText.getCurrentSatRadioTextArtistName();
        currentStationInfo_Status.tertiaryInformation.setContent(string2);
        currentStationInfo_Status.ti_Type = string2.length() != 0 ? 73 : 0;
        currentStationInfo_Status.quaternaryInformation
                .setContent(radioCurrentSatRadioText.getCurrentSatRadioTextCategoryName());
        currentStationInfo_Status.qi_Type = 16;
    }

    private void setStationInfoForRadio(CurrentStationInfo_Status currentStationInfo_Status) {
        switch (this.getRadioService().getCurrentStationBand()) {
            case 0: {
                this.setStationInfoForRadioAM(currentStationInfo_Status);
                break;
            }
            case 1: {
                this.setStationInfoForRadioFM(currentStationInfo_Status);
                break;
            }
            case 2: {
                this.setStationInfoForRadioDAB(currentStationInfo_Status);
                break;
            }
            case 3: {
                this.setStationInfoForRadioSirius(currentStationInfo_Status);
                break;
            }
            default: {
                this.setStationInfoNoInformationAvailable(currentStationInfo_Status);
            }
        }
    }

    private void setCurrentStationInfoForTvTuner(CurrentStationInfo_Status currentStationInfo_Status) {
        TvTunerService tvTunerService = this.getTvTunerService();
        switch (tvTunerService.getCurrentTvTunerSource()) {
            case 1: {
                currentStationInfo_Status.primaryInformation.setContent("AV");
                break;
            }
            default: {
                currentStationInfo_Status.primaryInformation.setContent(tvTunerService.getCurrentTvTunerStationName());
            }
        }
        currentStationInfo_Status.pi_Type = 71;
        currentStationInfo_Status.pi_Id = 0;
    }

    private boolean setStationInfoStatus(CurrentStationInfo_Status currentStationInfo_Status) {
        boolean bl;
        int n = this.getSoundService().getCurrentAudioComponent();
        if (n != this.lastAudioComponent && n != 0) {
            this.lastAudioComponent = n;
            this.setStationInfoNoInformationAvailable(currentStationInfo_Status);
            bl = true;
        } else {
            if (n == 0) {
                if (!this.getSuppressionTimer().isRunning()) {
                    this.getSuppressionTimer().retrigger(new Integer(2));
                }
            } else {
                this.lastAudioComponent = n;
            }
            switch (this.lastAudioComponent) {
                case 2:
                case 3: {
                    this.setStationInfoForMedia(currentStationInfo_Status);
                    break;
                }
                case 5: {
                    this.setCurrentStationInfoForTvTuner(currentStationInfo_Status);
                    break;
                }
                case 1: {
                    this.setStationInfoForRadio(currentStationInfo_Status);
                    break;
                }
                case 6: {
                    this.setStationInfoForMirrorLink(currentStationInfo_Status);
                    break;
                }
                case 4: {
                    this.setStationInfoForExbox(currentStationInfo_Status);
                    break;
                }
                default: {
                    this.setStationInfoNoInformationAvailable(currentStationInfo_Status);
                }
            }
            bl = false;
        }
        return bl;
    }

    private void setStationInfoSwitches(CurrentStationInfo_Status currentStationInfo_Status) {
        RadioService radioService = this.getRadioService();
        NavigationService navigationService = this.getNavigationService();
        currentStationInfo_Status.stationInfoSwitches.taTpAvailable = radioService.getTPState() == 0;
        currentStationInfo_Status.stationInfoSwitches.tmcAvailable = radioService.isRDSSetupOptionEnabled()
                && navigationService.isTMCSignalAvailable();
        currentStationInfo_Status.stationInfoSwitches.vicsAvailableJapanMarketOnly = navigationService
                .isVISCSignalAvailable();
        currentStationInfo_Status.stationInfoSwitches.ibocHdRadioAvailable = this.isHDRadioAvailable();
    }

    private void sendCurrentStationInfo(CurrentStationInfo_Status currentStationInfo_Status, boolean bl) {
        boolean bl2 = this.getDelegate().getPropertyListener(this).statusProperty(currentStationInfo_Status, this);
        if (bl2 && !this.timer.isRunning()) {
            this.timer.retrigger(bl ? new Integer(1) : new Integer(0));
        } else if (bl) {
            if (!this.timer.isRunning()) {
                this.timer.retrigger(new Integer(1));
            } else {
                this.timer.setUserInfo(new Integer(1));
            }
        }
    }

    // @Override
    public void getProperty(BAPEntity bAPEntity, PropertyListener propertyListener) {
        propertyListener.requestError(65, this);
    }

    // @Override
    public void requestAcknowledge() {
        this.setCurrentStationInfoSend(null);
    }

    // @Override
    public void indicationError(int n, BAPFunctionListener bAPFunctionListener) {
    }

    // @Override
    public void errorAcknowledge() {
    }

    // @Override
    public void initialize(boolean bl) {
        this.timer.stop();
        this.getSuppressionTimer().stop();
    }

    // @Override
    public void uninitialize() {
        this.timer.stop();
        this.getSuppressionTimer().stop();
        this.getRadioService().removeRadioServiceListener(this, RADIO_LISTENER_IDS);
        this.getMediaService().removeMediaServiceListener(this, MEDIA_LISTENER_IDS);
        this.getTvTunerService().removeTvTunerServiceListener(this, TV_TUNER_LISTENER_IDS);
        this.getSmartphoneIntegrationService().removeSmartphoneIntegrationServiceListener(this,
                SMARTPHONE_INTEGRATION_LISTENER_IDS);
        this.getNavigationService().removeNavigationServiceListener(this, NAVIGATION_LISTENER_IDS);
        this.getSoundService().removeSoundServiceListener(this, SOUND_LISTENER_IDS);
        this.getExboxService().removeExboxServiceListener(this, EXBOX_LISTENER_IDS);
        this.getConfigurationService().removeLanguageChangeListener(this);
    }

    // @Override
    public void setGetProperty(BAPEntity bAPEntity, PropertyListener propertyListener) {
        propertyListener.requestError(65, this);
    }

    // @Override
    public void ackProperty(BAPEntity bAPEntity, PropertyListener propertyListener) {
        propertyListener.requestError(65, this);
    }

    protected void setCurrentStationHandleSend(Integer n) {
        this.currentAbsolutePosition = n.intValue();
    }

    protected void setTransmitLongDabPs(Boolean bl) {
        boolean bl2 = bl.booleanValue();
        if (this.transmitALongDABProgrammStation != bl2) {
            this.transmitALongDABProgrammStation = bl2;
            this.process(-1);
        }
    }

    // @Override
    public void process(int n) {
        if (!this.timer.isRunning()) {
            CurrentStationInfo_Status currentStationInfo_Status = this.dequeueBAPEntity();
            boolean bl = this.computeCurrentStationInfoStatus(currentStationInfo_Status);
            System.out.println("AADEBUG: CurrenStationInfo: primary: " + currentStationInfo_Status.primaryInformation.toString() + " primary: "+ currentStationInfo_Status.pi_Type + " pi_id: "+ currentStationInfo_Status.pi_Id);
            System.out.println("AADEBUG: CurrenStationInfo: secondaryInformation: " + currentStationInfo_Status.secondaryInformation + " primary: "+ currentStationInfo_Status.si_Type);
            System.out.println("AADEBUG: CurrenStationInfo: tertiaryInformation: " + currentStationInfo_Status.tertiaryInformation + " primary: "+ currentStationInfo_Status.ti_Type);
            System.out.println("AADEBUG: CurrenStationInfo: quaternaryInformation: " + currentStationInfo_Status.quaternaryInformation + " primary: "+ currentStationInfo_Status.qi_Type);

            this.sendCurrentStationInfo(currentStationInfo_Status, bl);
        } else {
            this.timer.setUserInfo(new Integer(1));
        }
    }

    // @Override
    public void timerFired(Timer timer) {
        int n = ((Integer) timer.getUserInfo()).intValue();
        switch (n) {
            case 1: {
                this.process(-1);
                break;
            }
            case 2: {
                if (this.lastAudioComponent == 0 || this.getSoundService().getCurrentAudioComponent() != 0)
                    break;
                this.lastAudioComponent = 0;
                this.process(-1);
                break;
            }
        }
    }

    private boolean computeCurrentStationInfoStatus(CurrentStationInfo_Status currentStationInfo_Status) {
        boolean bl = this.setStationInfoStatus(currentStationInfo_Status);
        this.setStationInfoSwitches(currentStationInfo_Status);
        this.setStationProperties(currentStationInfo_Status);
        return bl;
    }

    protected void setAutostoreRunning(Boolean bl) {
        if (this.autostoreRunning != bl.booleanValue()) {
            this.autostoreRunning = bl.booleanValue();
            this.process(-1);
        }
    }

    protected void setManualTuneMode(Boolean bl) {
        this.manualTuning = bl.booleanValue();
        this.process(-1);
    }

    // @Override
    public void processHMIEvent(int n) {
    }

    // @Override
    public void updateNavigationData(NavigationService navigationService, int n) {
        this.process(-1);
    }

    // @Override
    public void updateSmartphoneIntegrationData(SmartphoneIntegrationService smartphoneIntegrationService, int n) {
        this.process(-1);
    }

    // @Override
    public void updateTvTunerData(TvTunerService tvTunerService, int n) {
        this.process(-1);
    }

    // @Override
    public void updateSoundData(SoundService soundService, int n) {
        this.process(-1);
    }

    // @Override
    public void updateMediaData(MediaService mediaService, int n) {
        this.process(-1);
    }

    // @Override
    public void updateRadioData(RadioService radioService, int n) {
        this.process(-1);
    }

    // @Override
    public void updateExboxData(ExboxService exboxService, int n) {
        this.process(-1);
    }

    // @Override
    public void languageChanged() {
        this.process(-1);
    }

    static /* synthetic */ Class class$(String string) {
        try {
            return Class.forName(string);
        } catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError(string);
        }
    }

    static {
        RADIO_LISTENER_IDS = new int[] { 1410, 1339, 1324, 1320, 1321, 1371, 1372, 3404, 3403, 1443, 1436, 2323, 1409,
                1423, 1356, 1357, 1352, 1354, 533079040, 516301824, 1445, 1446, 1366, 1426, 1454, 1456, 1455, 1308,
                3962 };
        MEDIA_LISTENER_IDS = new int[] { 8, 3520, 124, 125, 138, 2849, 58 };
        TV_TUNER_LISTENER_IDS = new int[] { 2314 };
        SMARTPHONE_INTEGRATION_LISTENER_IDS = new int[] { 1114057728 };
        NAVIGATION_LISTENER_IDS = new int[] { 2389, 2388 };
        SOUND_LISTENER_IDS = new int[] { 1470 };
        EXBOX_LISTENER_IDS = new int[] { -1995243392 };
    }
}
