package tulv.vocab.fragment;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import tulv.vocab.Entities.Vocab;
import tulv.vocab.R;
import tulv.vocab.layout.VocabLayout;
import tulv.vocab.model.VocabModel;

/**
 * Created by tulv on 10/19/2016.
 */
public class VocabFragment extends Fragment {
    VocabModel vocabModel;
    ArrayList<Vocab> arrayList;
    Vocab vocab;
    Context context;
    MediaPlayer mediaPlayer;
    public static final String EXTRA_POSITION = "EXTRA_POSITION";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        ViewHolder viewHolder=new ViewHolder();
        LinearLayout l = (LinearLayout)
                inflater.inflate(R.layout.fragment_vocab, container, false);
        viewHolder.tvEnglish = (TextView) l.findViewById(R.id.tvEnglish);
        viewHolder.tvVietnamese = (TextView) l.findViewById(R.id.tvVietnamese);
        viewHolder.tvPronoun = (TextView) l.findViewById(R.id.tvPronoun);
        viewHolder.btnSpeak = (ImageButton) l.findViewById(R.id.btSpeak);
        viewHolder.btnRemind = (ImageButton) l.findViewById(R.id.btRemind);
        viewHolder.ivImage = (ImageView) l.findViewById(R.id.imgVocab);
        vocabModel = new VocabModel(getContext());
        int pos = this.getArguments().getInt("pos");
        arrayList = new ArrayList<>();
        arrayList = getArguments().getParcelableArrayList("list");
        pos = pos % arrayList.size();
        vocab = new Vocab();
        vocab = arrayList.get(pos);

        byte[] blod = vocab.getImage();
        ByteArrayInputStream im = new ByteArrayInputStream(blod);
        viewHolder.ivImage.setImageBitmap(BitmapFactory.decodeStream(im));

        viewHolder.tvEnglish.setText(vocab.getEnglish());
        viewHolder.tvVietnamese.setText(vocab.getVietnamese());
        viewHolder.tvPronoun.setText(vocab.getPronoun());

        if (vocab.getRemind() == true) {
            int resID = getResources().getIdentifier("alarmclockicon", "drawable", "tulv.vocab");
            viewHolder.btnRemind.setImageResource(resID);
        } else {
            int resID = getResources().getIdentifier("alarmclockdisabledicon", "drawable", "tulv.vocab");
            viewHolder.btnRemind.setImageResource(resID);
        }

        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.btnRemind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vocabModel.updateRemind(vocab.getId());
                vocab = vocabModel.getVocab(vocab.getId());
                boolean s = vocab.getRemind();
                if (s == false) {
                    int resID = getResources().getIdentifier("alarmclockdisabledicon", "drawable", "tulv.vocab");
                    finalViewHolder.btnRemind.setImageResource(resID);
                } else {
                    int resID = getResources().getIdentifier("alarmclockicon", "drawable", "tulv.vocab");
                    finalViewHolder.btnRemind.setImageResource(resID);
                }
            }
        });
        VocabLayout root = (VocabLayout) l.findViewById(R.id.root);
        float scale = this.getArguments().getFloat("scale");
        root.setScaleBoth(scale);
        viewHolder.btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eng=vocab.getEnglish().replace(' ','_');
                int resID = getResources().getIdentifier(eng, "raw", "tulv.vocab");
                mediaPlayer=MediaPlayer.create(getContext(), resID);
                mediaPlayer.start();
            }

        });
              return l;
    }
    public class ViewHolder{
        TextView tvEnglish;
        TextView tvVietnamese;
        TextView tvPronoun;
        ImageView ivImage;
        ImageButton btnRemind;
        ImageButton btnSpeak;
    }
}