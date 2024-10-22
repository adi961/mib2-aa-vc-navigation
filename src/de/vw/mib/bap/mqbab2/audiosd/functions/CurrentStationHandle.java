/*
 * Decompiled with CFR 0.152.
 */
package de.vw.mib.bap.mqbab2.audiosd.functions;

import de.vw.mib.bap.datatypes.BAPEntity;
import de.vw.mib.bap.functions.BAPFunctionListener;
import de.vw.mib.bap.functions.Property;
import de.vw.mib.bap.functions.PropertyListener;
import de.vw.mib.bap.mqbab2.audiosd.api.ASLAudioSDConstants;
import de.vw.mib.bap.mqbab2.common.api.media.MediaService;
import de.vw.mib.bap.mqbab2.common.api.media.MediaServiceListener;
import de.vw.mib.bap.mqbab2.common.api.radio.RadioService;
import de.vw.mib.bap.mqbab2.common.api.radio.RadioServiceListener;
import de.vw.mib.bap.mqbab2.common.api.radio.datatypes.iterator.elements.RadioReceptionListElement;
import de.vw.mib.bap.mqbab2.common.api.sound.SoundService;
import de.vw.mib.bap.mqbab2.common.api.sound.SoundServiceListener;
import de.vw.mib.bap.mqbab2.common.api.stages.BAPStage;
import de.vw.mib.bap.mqbab2.common.api.stages.BAPStageInitializer;
import de.vw.mib.bap.mqbab2.common.api.stages.Function;
import de.vw.mib.bap.mqbab2.common.api.tvtuner.TvTunerService;
import de.vw.mib.bap.mqbab2.common.api.tvtuner.TvTunerServiceListener;
import de.vw.mib.bap.mqbab2.common.arrays.FsgArrayListComplete;
import de.vw.mib.bap.mqbab2.common.arrays.FsgArrayListWindowed;
import de.vw.mib.bap.mqbab2.generated.audiosd.serializer.CurrentStation_Handle_Status;
import java.util.Iterator;

public class CurrentStationHandle
extends Function
implements Property,
ASLAudioSDConstants,
MediaServiceListener,
RadioServiceListener,
SoundServiceListener,
TvTunerServiceListener {
    private CurrentStation_Handle_Status lastCurrentStationHandleStatus = null;
    private FsgArrayListComplete fullReceptionList = null;
    private FsgArrayListComplete fullRadioTvPresetList = null;
    private FsgArrayListWindowed currentMediaBrowserList;
    private boolean autoStoreRunning = false;
    private static /*final*/ int CURRENT_STATION_IN_RECEPTIONLIST_BAP_POS_ID;
    private static /*final*/ int OFFSET_FOR_REF_HANDLE;
    private static /*final*/ int NO_HANDLE_REF_EXISTS;
    private static /*final*/ int INVALID_HANDLE_REF;
    private static /*final*/ long INVALID_DAB_ID;
    private static /*final*/ int INVALID_INDEX;
    private static /*final*/ int DAB_NO_ABS_POS_ID_EXITS;
    private static /*final*/ int INVALID_BAP_POS_ID;
    private static /*final*/ int INDEX_ID;
    private static /*final*/ int INDEX_ID_ABS_POSITION;
    private static /*final*/ int INDEX_ENSEMBLE_ID;
    private static /*final*/ int INDEX_PARENT_ABS_POSITION;
    private static /*final*/ int INDEX_ENSEMBLE_ID_ABS_POS;
    protected static /*final*/ int[] MEDIA_LISTENER_IDS;
    protected static /*final*/ int[] RADIO_LISTENER_IDS;
    protected static /*final*/ int[] TV_TUNER_LISTENER_IDS;
    protected static /*final*/ int[] SOUND_LISTENER_IDS;
    static /* synthetic */ Class class$de$vw$mib$bap$mqbab2$generated$audiosd$serializer$CurrentStation_Handle_Status;

    public static CurrentStationHandle instance = null;

   // @Override
    public BAPEntity init(BAPStageInitializer bAPStageInitializer) {
        instance = this;
        this.getMediaService().addMediaServiceListener(this, MEDIA_LISTENER_IDS);
        this.getRadioService().addRadioServiceListener(this, RADIO_LISTENER_IDS);
        this.getSoundService().addSoundServiceListener(this, SOUND_LISTENER_IDS);
        this.getTvTunerService().addTvTunerServiceListener(this, TV_TUNER_LISTENER_IDS);
        return this.computeCurrentStationHandleStatus();
    }

    protected CurrentStation_Handle_Status dequeueBAPEntity() {
        return (CurrentStation_Handle_Status)this.context.dequeueBAPEntity(this, class$de$vw$mib$bap$mqbab2$generated$audiosd$serializer$CurrentStation_Handle_Status == null ? (class$de$vw$mib$bap$mqbab2$generated$audiosd$serializer$CurrentStation_Handle_Status = CurrentStationHandle.class$("de.vw.mib.bap.mqbab2.generated.audiosd.serializer.CurrentStation_Handle_Status")) : class$de$vw$mib$bap$mqbab2$generated$audiosd$serializer$CurrentStation_Handle_Status);
    }

   // @Override
    public void setFunctionData(BAPStage bAPStage, Object object) {
        switch (bAPStage.getFunctionId()) {
            case 23: {
                this.setFullReceptionList((FsgArrayListComplete)object);
                break;
            }
            case 33: {
                this.setFullRadioTvPresetList((FsgArrayListComplete)object);
                break;
            }
            case 30: {
                this.setAutostoreRunning((Boolean)object);
                break;
            }
            case 36: {
                this.setCurrentMediaBrowserArrayList((FsgArrayListWindowed)object);
                break;
            }
        }
    }

    protected void setCurrentStationHandleSend(Integer n) {
        int[] nArray = new int[]{24, 21};
        this.context.updateStages(this, nArray, n);
    }

    protected void setCurrentStationHandleOutput(CurrentStation_Handle_Status currentStation_Handle_Status) {
        int[] nArray = new int[]{24};
        this.context.updateStages(this, nArray, currentStation_Handle_Status);
    }

   // @Override
    public int getFunctionId() {
        return 22;
    }

    private int[] findIdInPresetList(int n, int n2) {
        int n3;
        int n4;
        if (this.fullRadioTvPresetList != null && n2 != -1) {
            n4 = this.fullRadioTvPresetList.getBapPosID(AudioSDCommon.computeUniquePresetID(n, n2));
            if (n4 != 0) {
                n3 = this.fullRadioTvPresetList.getIndexOfBapPosId(n4);
                if (n3 == -1) {
                    n4 = 0;
                    n3 = 0;
                }
            } else {
                n3 = 0;
            }
        } else {
            n4 = 0;
            n3 = 0;
        }
        int[] nArray = new int[]{n4, ++n3};
        return nArray;
    }

    private int[] findIdInAmFmSiriusReceptionList(long l) {
        long l2;
        int[] nArray = this.findIdsInMappingTable(l);
        if (nArray[1] == -1 && this.fullReceptionList != null && (l2 = this.fullReceptionList.getInternalUserId(1)) != -1L) {
            nArray = this.findIdsInMappingTable(l2);
        }
        if (nArray[1] == -1) {
            nArray[1] = 0;
            nArray[0] = -1;
        }
        return nArray;
    }

    private int[] findIdInFMReceptionList(long l) {
        long l2;
        int[] nArray = this.findIdInAmFmSiriusReceptionList(l);
        if (nArray[0] == -1 && (l2 = AudioSDCommon.filterOutPIFromUniqueID(l)) != l) {
            nArray = this.findIdInAmFmSiriusReceptionList(AudioSDCommon.filterOutPIFromUniqueID(l));
        }
        return nArray;
    }

    private int[] findIdsInMappingTable(long l) {
        int n;
        int n2;
        int n3;
        int n4;
        int n5 = n4 = this.fullReceptionList != null ? this.fullReceptionList.getBapPosID(l) : 0;
        if (n4 != 0 && this.fullReceptionList != null) {
            n3 = n4;
            n2 = this.fullReceptionList.getIndexOfBapPosId(n4);
            n2 = n2 != -1 ? n2 + 1 : n2;
            n = (int)this.fullReceptionList.getInternalUserId(n4);
        } else {
            n3 = -1;
            n2 = -1;
            n = -1;
        }
        int[] nArray = new int[]{n3, n2, n};
        return nArray;
    }

    private long getParentID(RadioReceptionListElement radioReceptionListElement, int n) {
        long l = n > -1 && radioReceptionListElement != null ? radioReceptionListElement.getReceptionListElementParentUniqueId() : -1L;
        return l;
    }

    private RadioReceptionListElement getReceptionListElementAtPosition(int n) {
        Iterator iterator = this.getRadioService().getReceptionList();
        int n2 = 0;
        while (iterator.hasNext()) {
            RadioReceptionListElement radioReceptionListElement = (RadioReceptionListElement)iterator.next();
            if (n2 == n) {
                return radioReceptionListElement;
            }
            ++n2;
        }
        return null;
    }

    private int[] findIdInDABReceptionList(long l) {
        int[] nArray;
        int n;
        RadioReceptionListElement radioReceptionListElement;
        long l2;
        int n2 = -1;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int[] nArray2 = this.findIdsInMappingTable(l);
        n2 = nArray2[0];
        n3 = nArray2[1];
        if (n3 == -1) {
            n2 = -1;
            n3 = 0;
        }
        if ((l2 = this.getParentID(radioReceptionListElement = this.getReceptionListElementAtPosition(n = nArray2[2]), n)) != -1L) {
            nArray = this.findIdsInMappingTable(l2);
            if ((radioReceptionListElement.getReceptionListElementType() & 4) == 4) {
                RadioReceptionListElement radioReceptionListElement2 = this.getReceptionListElementAtPosition(nArray[2]);
                long l3 = this.getParentID(radioReceptionListElement2, nArray[2]);
                nArray = this.findIdsInMappingTable(l3);
            } else {
                n4 = nArray[0];
                if (n4 == -1) {
                    n4 = 0;
                }
            }
            n5 = nArray[1];
            if (n5 == -1) {
                n5 = 0;
            }
        }
        nArray = new int[]{n2, n3, n4, n5};
        return nArray;
    }

    protected void setStationHandlesForRadio(CurrentStation_Handle_Status currentStation_Handle_Status) {
        switch (this.getRadioService().getCurrentStationBand()) {
            case 0: {
                long l = this.getRadioService().getCurrentStationIndices().getCurrentStationIndicesStationListActiveID();
                int[] nArray = this.findIdInAmFmSiriusReceptionList(l);
                currentStation_Handle_Status.fsgHandle = nArray[0];
                currentStation_Handle_Status.fsgHandle_absolutePosition = nArray[1];
                int[] nArray2 = this.findIdInPresetList(4, this.getRadioService().getCurrentStationIndices().getCurrentStationIndicesActivePresetIndex());
                currentStation_Handle_Status.presetList_Ref = nArray2[0];
                currentStation_Handle_Status.presetList_absolutePosition = nArray2[1];
                currentStation_Handle_Status.dab_EnsembleHandle = 0;
                currentStation_Handle_Status.dab_Ensemble_absolutePosition = 0;
                break;
            }
            case 2: {
                long l = this.getRadioService().getCurrentStationIndices().getCurrentStationIndicesStationListActiveID();
                int[] nArray = this.findIdInDABReceptionList(l);
                currentStation_Handle_Status.fsgHandle = nArray[0];
                currentStation_Handle_Status.fsgHandle_absolutePosition = nArray[1];
                int[] nArray3 = this.findIdInPresetList(7, this.getRadioService().getCurrentStationIndices().getCurrentStationIndicesActivePresetIndex());
                currentStation_Handle_Status.presetList_Ref = nArray3[0];
                currentStation_Handle_Status.presetList_absolutePosition = nArray3[1];
                currentStation_Handle_Status.dab_EnsembleHandle = nArray[2];
                currentStation_Handle_Status.dab_Ensemble_absolutePosition = nArray[3];
                break;
            }
            case 1: {
                long l = this.getRadioService().getCurrentStationIndices().getCurrentStationIndicesStationListActiveID();
                int[] nArray = this.findIdInFMReceptionList(l);
                currentStation_Handle_Status.fsgHandle = nArray[0];
                currentStation_Handle_Status.fsgHandle_absolutePosition = nArray[1];
                int[] nArray4 = this.findIdInPresetList(1, this.getRadioService().getCurrentStationIndices().getCurrentStationIndicesActivePresetIndex());
                currentStation_Handle_Status.presetList_Ref = nArray4[0];
                currentStation_Handle_Status.presetList_absolutePosition = nArray4[1];
                currentStation_Handle_Status.dab_EnsembleHandle = 0;
                currentStation_Handle_Status.dab_EnsembleHandle = 0;
                break;
            }
            case 3: {
                long l = this.getRadioService().getCurrentStationIndices().getCurrentStationIndicesStationListActiveID();
                int[] nArray = this.findIdInAmFmSiriusReceptionList(l);
                currentStation_Handle_Status.fsgHandle = nArray[0];
                currentStation_Handle_Status.fsgHandle_absolutePosition = nArray[1];
                int[] nArray5 = this.findIdInPresetList(6, this.getRadioService().getCurrentStationIndices().getCurrentStationIndicesActivePresetIndex());
                currentStation_Handle_Status.presetList_Ref = nArray5[0];
                currentStation_Handle_Status.presetList_absolutePosition = nArray5[1];
                currentStation_Handle_Status.dab_EnsembleHandle = 0;
                currentStation_Handle_Status.dab_Ensemble_absolutePosition = 0;
                break;
            }
            default: {
                this.setInvalidCurrentStationHandleStatus(currentStation_Handle_Status);
            }
        }
    }

    protected void setCurrentStationHandlesForTV(CurrentStation_Handle_Status currentStation_Handle_Status) {
        long l = this.getRadioService().getCurrentStationIndices().getCurrentStationIndicesStationListActiveID();
        int[] nArray = this.findIdInAmFmSiriusReceptionList(l);
        currentStation_Handle_Status.fsgHandle = nArray[0];
        currentStation_Handle_Status.fsgHandle_absolutePosition = nArray[1];
        int[] nArray2 = this.findIdInPresetList(9, this.getRadioService().getCurrentStationIndices().getCurrentStationIndicesActivePresetIndex());
        currentStation_Handle_Status.presetList_Ref = nArray2[0];
        currentStation_Handle_Status.presetList_absolutePosition = nArray2[1];
        currentStation_Handle_Status.dab_EnsembleHandle = 0;
        currentStation_Handle_Status.dab_Ensemble_absolutePosition = 0;
    }

    private int computeCurrentActiveMediaPosId() {
        MediaService mediaService = this.getMediaService();
        System.out.println("AADEBUG: computeCurrentActiveMediaPosId: getActiveTrackEntryId: " + mediaService.getActiveTrackInfo().getActiveTrackEntryId() + " getActiveTrackContentType: " + mediaService.getActiveTrackInfo().getActiveTrackContentType());
        int n = this.currentMediaBrowserList != null ? this.currentMediaBrowserList.getBapPosIdOrGenerate(MediaBrowser.createObjectID(mediaService.getActiveTrackInfo().getActiveTrackEntryId(), mediaService.getActiveTrackInfo().getActiveTrackContentType())) : 0;
        return n;
    }

    private void setCurrentStationHandleForMedia(CurrentStation_Handle_Status currentStation_Handle_Status) {
        MediaService mediaService = this.getMediaService();
        System.out.println("AADEBUG: getActiveTrackAbsolutePosition: "+ mediaService.getActiveTrackInfo().getActiveTrackAbsolutePosition());
        int n = this.computeCurrentActiveMediaPosId();
        currentStation_Handle_Status.fsgHandle = n == 0 ? -1 : n;
        currentStation_Handle_Status.fsgHandle_absolutePosition = mediaService.getActiveTrackInfo().getActiveTrackAbsolutePosition() + 1;
        currentStation_Handle_Status.presetList_Ref = 0;
        currentStation_Handle_Status.dab_EnsembleHandle = 0;
        currentStation_Handle_Status.dab_EnsembleHandle = 0;
    }

    private void setInvalidCurrentStationHandleStatus(CurrentStation_Handle_Status currentStation_Handle_Status) {
        System.out.println("AADEBUG: CurrentStationHandle: setInvalidCurrentStationHandleStatus");
        currentStation_Handle_Status.fsgHandle = -1;
        currentStation_Handle_Status.fsgHandle_absolutePosition = 0;
        currentStation_Handle_Status.presetList_Ref = 0;
        currentStation_Handle_Status.dab_EnsembleHandle = 0;
        currentStation_Handle_Status.dab_EnsembleHandle = 0;
    }

    private void setCurrentStationHandleData(CurrentStation_Handle_Status currentStation_Handle_Status) {
        System.out.println("AADEBUG: CurrentStationHandle: getCurrentAudioComponent: " + this.getSoundService().getCurrentAudioComponent() + " isBapMediaBrowserAvailable: "+ this.getMediaService().isBapMediaBrowserAvailable());
        switch (this.getSoundService().getCurrentAudioComponent()) {
            case 1: {
                if (this.autoStoreRunning) {
                    this.setInvalidCurrentStationHandleStatus(currentStation_Handle_Status);
                    break;
                }
                this.setStationHandlesForRadio(currentStation_Handle_Status);
                break;
            }
            case 5: {
                this.setCurrentStationHandlesForTV(currentStation_Handle_Status);
                break;
            }
            case 2: {
                if (this.getMediaService().isBapMediaBrowserAvailable()) {
                    this.setCurrentStationHandleForMedia(currentStation_Handle_Status);
                    break;
                }
                this.setInvalidCurrentStationHandleStatus(currentStation_Handle_Status);
                break;
            }
            case 6: {
                this.setCurrentStationHandleForMedia(currentStation_Handle_Status);
                break;
            }
            default: {
                this.setInvalidCurrentStationHandleStatus(currentStation_Handle_Status);
            }
        }
    }

    public void sendCurrentStationHandleStatus(CurrentStation_Handle_Status currentStation_Handle_Status) {
        if (this.getDelegate().getPropertyListener(this).statusProperty(currentStation_Handle_Status, this)) {
            this.lastCurrentStationHandleStatus = currentStation_Handle_Status;
            this.setCurrentStationHandleSend(new Integer(currentStation_Handle_Status.fsgHandle_absolutePosition));
        }
    }

   // @Override
    public void getProperty(BAPEntity bAPEntity, PropertyListener propertyListener) {
        propertyListener.requestError(65, this);
    }

   // @Override
    public void requestAcknowledge() {
        this.setCurrentStationHandleOutput(this.lastCurrentStationHandleStatus);
    }

   // @Override
    public void indicationError(int n, BAPFunctionListener bAPFunctionListener) {
    }

   // @Override
    public void errorAcknowledge() {
    }

   // @Override
    public void initialize(boolean bl) {
        this.autoStoreRunning = false;
    }

   // @Override
    public void uninitialize() {
        this.getMediaService().removeMediaServiceListener(this, MEDIA_LISTENER_IDS);
        this.getRadioService().removeRadioServiceListener(this, RADIO_LISTENER_IDS);
        this.getSoundService().removeSoundServiceListener(this, SOUND_LISTENER_IDS);
    }

   // @Override
    public void setGetProperty(BAPEntity bAPEntity, PropertyListener propertyListener) {
        propertyListener.requestError(65, this);
    }

   // @Override
    public void ackProperty(BAPEntity bAPEntity, PropertyListener propertyListener) {
        propertyListener.requestError(65, this);
    }

   // @Override
    public void process(int n) {
        this.sendCurrentStationHandleStatus(this.computeCurrentStationHandleStatus());
    }

    private CurrentStation_Handle_Status computeCurrentStationHandleStatus() {
        CurrentStation_Handle_Status currentStation_Handle_Status = this.dequeueBAPEntity();
        this.setCurrentStationHandleData(currentStation_Handle_Status);

        System.out.println("AADEBUG: CurrentStationHandle: fsgHandle: "+ currentStation_Handle_Status.fsgHandle + " fsgHandle_absolutePosition: "+ currentStation_Handle_Status.fsgHandle_absolutePosition);
        return currentStation_Handle_Status;
    }

    protected void setFullReceptionList(FsgArrayListComplete fsgArrayListComplete) {
        boolean bl = this.fullReceptionList != null;
        this.fullReceptionList = fsgArrayListComplete;
        if (bl) {
            this.process(-1);
        }
    }

    protected void setFullRadioTvPresetList(FsgArrayListComplete fsgArrayListComplete) {
        this.fullRadioTvPresetList = fsgArrayListComplete;
        this.process(-1);
    }

    protected void setAutostoreRunning(Boolean bl) {
        this.autoStoreRunning = bl.booleanValue();
        this.process(-1);
    }

    protected void setCurrentMediaBrowserArrayList(FsgArrayListWindowed fsgArrayListWindowed) {
        this.currentMediaBrowserList = fsgArrayListWindowed;
        this.process(-1);
    }

   // @Override
    public void processHMIEvent(int n) {
    }

   // @Override
    public void updateSoundData(SoundService soundService, int n) {
        this.process(-1);
    }

   // @Override
    public void updateRadioData(RadioService radioService, int n) {
        this.process(-1);
    }

   // @Override
    public void updateMediaData(MediaService mediaService, int n) {
        this.process(-1);
    }

   // @Override
    public void updateTvTunerData(TvTunerService tvTunerService, int n) {
        this.process(-1);
    }

    static /* synthetic */ Class class$(String string) {
        try {
            return Class.forName(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError(string);
        }
    }

    static {
        MEDIA_LISTENER_IDS = new int[]{810013, 810015};
        RADIO_LISTENER_IDS = new int[]{1324, 2486, 1412};
        TV_TUNER_LISTENER_IDS = new int[]{2662};
        SOUND_LISTENER_IDS = new int[]{1470};
    }
}
