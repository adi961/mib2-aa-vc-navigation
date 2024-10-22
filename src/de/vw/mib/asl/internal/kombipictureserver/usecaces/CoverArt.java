/*
 * Decompiled with CFR 0.152.
 */
package de.vw.mib.asl.internal.kombipictureserver.usecaces;

import de.vw.mib.asl.internal.kombipictureserver.common.api.bap.audiosd.BapAudioSdDelegate;
import de.vw.mib.asl.internal.kombipictureserver.common.api.bap.audiosd.BapAudioSdService;
import de.vw.mib.asl.internal.kombipictureserver.common.api.media.asl.MediaCoverArtDelegate;
import de.vw.mib.asl.internal.kombipictureserver.common.api.media.asl.MediaCoverArtService;
import de.vw.mib.asl.internal.kombipictureserver.common.util.KombiPictureServerUtil;
import de.vw.mib.asl.internal.kombipictureserver.usecaces.PictureServerUseCase;
import org.dsi.ifc.base.DSIBase;
import org.dsi.ifc.base.DSIListener;
import org.dsi.ifc.global.ResourceLocator;
import org.dsi.ifc.kombipictureserver.DSIKombiPictureServer;

public class CoverArt
extends PictureServerUseCase
implements MediaCoverArtDelegate,
BapAudioSdDelegate {
    private DSIKombiPictureServer _kombiPictureServer;
    private DSIListener _kombiPictureServerServiceResponder;
    private MediaCoverArtService _mediaCoverArtService;
    private BapAudioSdService _bapAudioSdService;
    private static /*final*/ int[] BAP_AUDIO_SD_SERVICES = new int[]{16, 22};
    private int _bapActiveSourceType;
    private ResourceLocator _mediaCoverArtResourceLocator;
    private boolean isWaitingForIsCoverValid;
    private static /*final*/ int BAP_AUDIO_SD_INVALID_HANDLE;
    static /* synthetic */ Class class$org$dsi$ifc$kombipictureserver$DSIKombiPictureServer;
    static /* synthetic */ Class class$org$dsi$ifc$kombipictureserver$DSIKombiPictureServerListener;
    static /* synthetic */ Class class$de$vw$mib$asl$internal$kombipictureserver$common$api$media$asl$MediaCoverArtService;
    static /* synthetic */ Class class$de$vw$mib$asl$internal$kombipictureserver$common$api$bap$audiosd$BapAudioSdService;

    private DSIKombiPictureServer getKombiPictureServer() {
        if (this._kombiPictureServer == null) {
            this._kombiPictureServer = (DSIKombiPictureServer)this.getCategoryDelegate().getDsiService(this, class$org$dsi$ifc$kombipictureserver$DSIKombiPictureServer == null ? (class$org$dsi$ifc$kombipictureserver$DSIKombiPictureServer = CoverArt.class$("org.dsi.ifc.kombipictureserver.DSIKombiPictureServer")) : class$org$dsi$ifc$kombipictureserver$DSIKombiPictureServer);
            this._kombiPictureServerServiceResponder = this.getCategoryDelegate().addDsiServiceResponder(this, (DSIBase)this._kombiPictureServer, class$org$dsi$ifc$kombipictureserver$DSIKombiPictureServerListener == null ? (class$org$dsi$ifc$kombipictureserver$DSIKombiPictureServerListener = CoverArt.class$("org.dsi.ifc.kombipictureserver.DSIKombiPictureServerListener")) : class$org$dsi$ifc$kombipictureserver$DSIKombiPictureServerListener, null);
        }
        return this._kombiPictureServer;
    }

    private DSIListener getKombiPictureServerServiceResponder() {
        return this._kombiPictureServerServiceResponder;
    }

    private MediaCoverArtService getMediaCoverArtService() {
        if (this._mediaCoverArtService == null) {
            this._mediaCoverArtService = (MediaCoverArtService)this.getCategoryDelegate().getService(this, class$de$vw$mib$asl$internal$kombipictureserver$common$api$media$asl$MediaCoverArtService == null ? (class$de$vw$mib$asl$internal$kombipictureserver$common$api$media$asl$MediaCoverArtService = CoverArt.class$("de.vw.mib.asl.internal.kombipictureserver.common.api.media.asl.MediaCoverArtService")) : class$de$vw$mib$asl$internal$kombipictureserver$common$api$media$asl$MediaCoverArtService);
            this.getCategoryDelegate().registerServiceListener(this, class$de$vw$mib$asl$internal$kombipictureserver$common$api$media$asl$MediaCoverArtService == null ? (class$de$vw$mib$asl$internal$kombipictureserver$common$api$media$asl$MediaCoverArtService = CoverArt.class$("de.vw.mib.asl.internal.kombipictureserver.common.api.media.asl.MediaCoverArtService")) : class$de$vw$mib$asl$internal$kombipictureserver$common$api$media$asl$MediaCoverArtService, MediaCoverArtDelegate.MEDIA_COVER_ART_PROPERTIES);
        }
        return this._mediaCoverArtService;
    }

    private BapAudioSdService getBapAudioSdService() {
        if (this._bapAudioSdService == null) {
            this._bapAudioSdService = (BapAudioSdService)this.getCategoryDelegate().getService(this, class$de$vw$mib$asl$internal$kombipictureserver$common$api$bap$audiosd$BapAudioSdService == null ? (class$de$vw$mib$asl$internal$kombipictureserver$common$api$bap$audiosd$BapAudioSdService = CoverArt.class$("de.vw.mib.asl.internal.kombipictureserver.common.api.bap.audiosd.BapAudioSdService")) : class$de$vw$mib$asl$internal$kombipictureserver$common$api$bap$audiosd$BapAudioSdService);
            this.getCategoryDelegate().registerServiceListener(this, class$de$vw$mib$asl$internal$kombipictureserver$common$api$bap$audiosd$BapAudioSdService == null ? (class$de$vw$mib$asl$internal$kombipictureserver$common$api$bap$audiosd$BapAudioSdService = CoverArt.class$("de.vw.mib.asl.internal.kombipictureserver.common.api.bap.audiosd.BapAudioSdService")) : class$de$vw$mib$asl$internal$kombipictureserver$common$api$bap$audiosd$BapAudioSdService, BAP_AUDIO_SD_SERVICES);
        }
        return this._bapAudioSdService;
    }

   // @Override
    public void initialize() {
    }

    public void uninitialize() {
        this.getCategoryDelegate().removeDsiServiceResponder(this, class$org$dsi$ifc$kombipictureserver$DSIKombiPictureServerListener == null ? (class$org$dsi$ifc$kombipictureserver$DSIKombiPictureServerListener = CoverArt.class$("org.dsi.ifc.kombipictureserver.DSIKombiPictureServerListener")) : class$org$dsi$ifc$kombipictureserver$DSIKombiPictureServerListener, this.getKombiPictureServerServiceResponder(), null, (DSIBase)this._kombiPictureServer);
        this.getCategoryDelegate().removeServiceListener(this, class$de$vw$mib$asl$internal$kombipictureserver$common$api$media$asl$MediaCoverArtService == null ? (class$de$vw$mib$asl$internal$kombipictureserver$common$api$media$asl$MediaCoverArtService = CoverArt.class$("de.vw.mib.asl.internal.kombipictureserver.common.api.media.asl.MediaCoverArtService")) : class$de$vw$mib$asl$internal$kombipictureserver$common$api$media$asl$MediaCoverArtService, MediaCoverArtDelegate.MEDIA_COVER_ART_PROPERTIES);
        this.getCategoryDelegate().removeServiceListener(this, class$de$vw$mib$asl$internal$kombipictureserver$common$api$bap$audiosd$BapAudioSdService == null ? (class$de$vw$mib$asl$internal$kombipictureserver$common$api$bap$audiosd$BapAudioSdService = CoverArt.class$("de.vw.mib.asl.internal.kombipictureserver.common.api.bap.audiosd.BapAudioSdService")) : class$de$vw$mib$asl$internal$kombipictureserver$common$api$bap$audiosd$BapAudioSdService, BAP_AUDIO_SD_SERVICES);
    }

    private void setBapActiveSourceType(int n) {
        boolean bl;
        boolean bl2 = bl = this._bapActiveSourceType == 0;
        if (this._bapActiveSourceType != n) {
            this._bapActiveSourceType = n;
            if (bl) {
                this._updateCoverArt();
            }
        }
    }

    private int getBapActiveSourceType() {
        return this._bapActiveSourceType;
    }

    private void setMediaCoverArtResourceLocator(ResourceLocator resourceLocator) {
        if (!KombiPictureServerUtil.compareResourceLocator(resourceLocator, this._mediaCoverArtResourceLocator)) {
            this._mediaCoverArtResourceLocator = resourceLocator;
            this._updateCoverArt();
        } else if (this.isWaitingForIsCoverValid && this.getMediaCoverArtService().isCoverValid()) {
            this.isWaitingForIsCoverValid = false;
            this._updateCoverArt();
        }
    }

    public void dsiKombiPictureServerIndicationCoverArt(long l, int n, int n2) {
        System.out.println("AADEBUG: l: " + l + ", n: " + n + ", n2: " + n2);
        boolean bl;
        boolean bl2 = bl = l == 0L && n == 0 && n2 == 255;
        if (bl) {
            this._updateCoverArt(true);
        } else {
            boolean bl3;
            boolean bl4 = bl3 = n == 0 && n2 == this.getBapActiveSourceType() && l == (long)this.getBapAudioSdService().getCurrentStationHandle().getCurrentStationHandleCurrentStationHandle() && this.getMediaCoverArtService().isCoverValid();
            if (bl3) {
                this._transmitCurrentCoverArt();
            } else {
                this._transmitCoverArt(l, n, n2, 2, null);
            }
        }
    }

    private static boolean _isActiveSourceSupportsCoverArt(int n) {
        boolean bl;
        switch (n) {
            case 6: 
            case 7: 
            case 8: 
            case 10: 
            case 11: 
            case 15: 
            case 16: 
            case 18: 
            case 19: {
                bl = true;
                break;
            }
            case 20: 
            case 21: 
            case 22: {
                bl = true;
                break;
            }
            default: {
                bl = false;
            }
        }
        return bl;
    }

    private void _transmitCoverArt(ResourceLocator resourceLocator) {
        if (resourceLocator != null) {
            System.out.println("AADEBUG: CoverArt resourceLocator: " + resourceLocator);
        } else {
            System.out.println("AADEBUG: CoverArt resourceLocator: null");
        }

        System.out.println("AADEBUG: getCurrentStationHandleCurrentStationHandle: " + this.getBapAudioSdService().getCurrentStationHandle().getCurrentStationHandleCurrentStationHandle());
        int n = resourceLocator != null ? 1 : 2;
        this._transmitCoverArt(this.getBapAudioSdService().getCurrentStationHandle().getCurrentStationHandleCurrentStationHandle(), 0, this.getBapActiveSourceType(), n, resourceLocator);
    }

    private void _transmitCoverArt(long l, int n, int n2, int n3, ResourceLocator resourceLocator) {
        this.getKombiPictureServer().responseCoverArt(l, n, n2, n3, resourceLocator);
        this.isWaitingForIsCoverValid = false;
    }

    private void _transmitCurrentCoverArt() {
        this._transmitCoverArt(this.getMediaCoverArtService().getCoverArt());
    }

    private void _updateCoverArt(boolean bl) {
        System.out.println("AADEBUG: _updateCoverArt: "+ bl + " activeSource: " + this.getBapActiveSourceType() + " isCoverValid: " + this.getMediaCoverArtService().isCoverValid());
        if (CoverArt._isActiveSourceSupportsCoverArt(this.getBapActiveSourceType())) {
            if (this.getMediaCoverArtService().isCoverValid()) {
                this._transmitCurrentCoverArt();
            } else if (bl) {
                this._transmitCoverArt(null);
            }
        } else {
            this._transmitCoverArt(-1L, 0, 0, 0, null);
        }
    }

    private void _updateCoverArt() {
        this._updateCoverArt(false);
    }

   // @Override
    public int getIdentifier() {
        return 1480001;
    }

   // @Override
    public void executeSequence() {
        this.getKombiPictureServer();
        this.getMediaCoverArtService();
        this.getBapAudioSdService();
        this.getCategoryDelegate().updateAllServiceListeners(class$de$vw$mib$asl$internal$kombipictureserver$common$api$bap$audiosd$BapAudioSdService == null ? (class$de$vw$mib$asl$internal$kombipictureserver$common$api$bap$audiosd$BapAudioSdService = CoverArt.class$("de.vw.mib.asl.internal.kombipictureserver.common.api.bap.audiosd.BapAudioSdService")) : class$de$vw$mib$asl$internal$kombipictureserver$common$api$bap$audiosd$BapAudioSdService);
    }

   // @Override
    public void updateMediaCoverArt(MediaCoverArtService mediaCoverArtService, int n) {
        this.setMediaCoverArtResourceLocator(mediaCoverArtService.getCoverArt());
    }

   // @Override
    public void updateBapAudioSd(BapAudioSdService bapAudioSdService, int n) {
        System.out.println("AADEBUG: CoverArt.updateBapAudioSd: n: "+ n);
        if (n == 16) {
            this.setBapActiveSourceType(bapAudioSdService.getActiveSource().getActiveSourceSourceType());
        } else if (n == 22 && this.getBapAudioSdService().getCurrentStationHandle().getCurrentStationHandleCurrentStationHandle() != -1 && CoverArt._isActiveSourceSupportsCoverArt(this.getBapActiveSourceType())) {
            this._updateCoverArt();
            if (!this.getMediaCoverArtService().isCoverValid()) {
                this.isWaitingForIsCoverValid = true;
            }
        }
    }

    static /* synthetic */ Class class$(String string) {
        try {
            return Class.forName(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError(string);
        }
    }
}

