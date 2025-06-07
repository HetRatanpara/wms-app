package com.example.wms.bottomsheet;

import android.app.Dialog;
import android.content.DialogInterface;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.wms.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class ScanNfcBottomSheet extends BottomSheetDialogFragment {

    private View view;
    private Button btnCancel;
    private NfcScanListener nfcScanListener;
    private NfcAdapter mNfcAdapter;


    public ScanNfcBottomSheet(NfcScanListener nfcScanListener) {
        this.nfcScanListener = nfcScanListener;
    }

    public ScanNfcBottomSheet(NfcScanListener nfcScanListener, boolean isFromSchedule) {
        this.nfcScanListener = nfcScanListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bottom_sheet_scan_nfc_tag, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                FrameLayout bottomSheet = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setSkipCollapsed(true);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        return bottomSheetDialog;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void init() {
        btnCancel = view.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nfcScanListener.onNfcScan(false, null, null, requireContext().getString(R.string.nfc_fail));
                dismiss();
            }
        });

        mNfcAdapter = NfcAdapter.getDefaultAdapter(getContext());
        if (mNfcAdapter != null) {
            /*int flags = NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;
            flags |= NfcAdapter.FLAG_READER_NFC_A;*/
            int flags = NfcAdapter.FLAG_READER_NFC_A;
            mNfcAdapter.enableReaderMode(getActivity(), new NfcAdapter.ReaderCallback() {
                @Override
                public void onTagDiscovered(Tag tag) {
                    String message = "";
                    final StringBuilder hexdump = new StringBuilder();
                    for (int i = 0; i < tag.getId().length; i++) {
                        String x = Integer.toHexString(((int) tag.getId()[i] & 0xff));
                        if (x.length() == 1) {
                            x = '0' + x;
                        }
                        hexdump.append(x);
                    }
                    Ndef ndef = Ndef.get(tag);
                    if (ndef != null) {
                        // NDEF is not supported by this Tag.
                        NdefMessage ndefMessage = ndef.getCachedNdefMessage();
                        if (ndefMessage != null) {
                            NdefRecord[] records = ndefMessage.getRecords();
                            if (records != null) {
                                for (NdefRecord ndefRecord : records) {
                                    if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN) {
                                        try {
                                            message = readText(ndefRecord);
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    }

                    final String finalMessage = message;
                    requireActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            nfcScanListener.onNfcScan(true, hexdump.toString(), finalMessage,
                                    requireContext().getString(R.string.message_nfc_tag_add_success));
                            dismiss();
                        }
                    });
                }
            }, flags, null);
        }
    }

    private String readText(NdefRecord record) throws UnsupportedEncodingException {
        /*
         * See NFC forum specification for "Text Record Type Definition" at 3.2.1
         *
         * http://www.nfc-forum.org/specs/
         *
         * bit_7 defines encoding
         * bit_6 reserved for future use, must be 0
         * bit_5..0 length of IANA language code
         */

        byte[] payload = record.getPayload();

        // Get the Text Encoding
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

        // Get the Language Code
        int languageCodeLength = payload[0] & 0063;

        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
        // e.g. "en"

        // Get the Text
        return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
       /* mNfcAdapter.disableReaderMode(requireActivity());*/
        super.onDismiss(dialog);
    }

    public interface NfcScanListener {
        void onNfcScan(boolean isSuccess, @Nullable String serialNo, @Nullable String tagText, String message);
    }
}
