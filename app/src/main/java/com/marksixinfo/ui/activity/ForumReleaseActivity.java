package com.marksixinfo.ui.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.marksixinfo.R;
import com.marksixinfo.base.ArrayCallback;
import com.marksixinfo.base.MarkSixActivity;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.bean.Base64UrlData;
import com.marksixinfo.bean.ForumModifyItemData;
import com.marksixinfo.bean.ReleaseLoadImage;
import com.marksixinfo.bean.ResultData;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.evenbus.DeleteInvitationEvent;
import com.marksixinfo.evenbus.MainClickIndexEvent;
import com.marksixinfo.interfaces.SucceedCallBackListener;
import com.marksixinfo.net.impl.CommArrayImpl;
import com.marksixinfo.net.impl.HeadlineImpl;
import com.marksixinfo.net.impl.ForumImpl;
import com.marksixinfo.utils.BASE64Utils;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.EventBusUtil;
import com.marksixinfo.utils.NumberUtils;
import com.marksixinfo.utils.ReleaseUtils;
import com.marksixinfo.widgets.CleanEditTextView;
import com.marksixinfo.widgets.ReleaseGridRvDividerDecoration;
import com.marksixinfo.widgets.easyemoji.KeyBoardUtils;
import com.marksixinfo.widgets.pictureselector.FullyGridLayoutManager;
import com.marksixinfo.widgets.pictureselector.GridImageAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 功能描述: 论坛发布/编辑
 *
 * @param:
 * @return:
 * @auther: Administrator
 * @date: 2019/4/2 0002 13:34
 */
public class ForumReleaseActivity extends MarkSixActivity implements GridImageAdapter.onAddPicClickListener,
        GridImageAdapter.OnItemClickListener, SucceedCallBackListener, TextWatcher, View.OnClickListener, View.OnTouchListener {


    @BindView(R.id.edit_title)
    CleanEditTextView editTitle;
    @BindView(R.id.edit_content)
    EditText editContent;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.elastic_content)
    NestedScrollView elasticContent;
//    @BindView(R.id.tv_release)
//    TextView tvRelease;

    private List<LocalMedia> selectList = new ArrayList<>();
    private GridImageAdapter adapter;
    private ReleaseUtils releaseUtils = new ReleaseUtils();
    private ArrayList<Base64UrlData> base64Url = new ArrayList<>();
    private String title;
    private String content;
    private int type;//0,发布 1,编辑
    private String id = "";
    private List<LocalMedia> netPic = new ArrayList<>();//临时存储网络图片
    private boolean isMoved;

    @Override
    public int getViewId() {
        return R.layout.activity_forum_release;
    }

    @Override
    public void afterViews() {

        type = NumberUtils.stringToInt(getStringParam(StringConstants.TYPE));
        id = getStringParam(StringConstants.ID);

        markSixTitle.init("论坛发帖", "",
                type == 0 ? "发布" : "修改", 0, this);

        editTitle.addTextChangedListener(this);
        editContent.addTextChangedListener(this);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 3,
                GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new ReleaseGridRvDividerDecoration(this));
        adapter = new GridImageAdapter(this, true, this);
        adapter.setList(selectList);
        adapter.setSelectMax(releaseUtils.getMaxSelectNum());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        editContent.setOnTouchListener(this);

        if (type == 1) {//编辑
            getModifyData();
        } else {
            KeyBoardUtils.setEditTextState(editTitle);
        }
    }

    /**
     * 获取编辑内容
     */
    private void getModifyData() {
        new ForumImpl(new MarkSixNetCallBack<ForumModifyItemData>(this, ForumModifyItemData.class) {
            @Override
            public void onSuccess(ForumModifyItemData response, int id) {
                if (response != null) {
                    setModifyData(response);
                }
            }
        }).getModifyData(id);
    }

    /**
     * 编辑帖子数据回显
     *
     * @param response
     */
    private void setModifyData(ForumModifyItemData response) {
        String title = response.getTitle();
        String content = response.getContent();
        List<String> pic = response.getPic();

        if (CommonUtils.StringNotNull(title)) {
            editTitle.setText(title);
            editTitle.setSelection(title.length());
        }

        if (CommonUtils.StringNotNull(content)) {
            editContent.setText(content);
            editContent.setSelection(content.length());
        }

        if (CommonUtils.ListNotNull(pic)) {
            netPic.clear();
            for (String s : pic) {
                netPic.add(releaseUtils.getOriginUrl(s));
            }
            selectList.addAll(0, netPic);
            adapter.setList(selectList);
            adapter.notifyDataSetChanged();
        }

        KeyBoardUtils.setEditTextState(editTitle);
    }

    /**
     * 发布
     */
    private void release() {
        showDialog();
        toBase64();
    }


    /**
     * 开线程转base64
     */
    private void toBase64() {
        base64Url.clear();
        List<LocalMedia> localPath = releaseUtils.getLocalPath(selectList);
        if (CommonUtils.ListNotNull(selectList) && CommonUtils.ListNotNull(localPath)) {
            for (LocalMedia media : selectList) {
                String compressPath = media.getCompressPath();
                if (CommonUtils.StringNotNull(compressPath)) {
                    File file = new File(compressPath);
                    String prefix = releaseUtils.getPrefix(media.getPictureType());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            base64Url.add(new Base64UrlData(BASE64Utils.fileToBase64(file, prefix), ""));
                            loadImageUrl();
                        }
                    }).start();
                }
            }
        } else {
            releaseContent();
        }
    }

    /**
     * 全部图片转换成功后,开始上传图片
     */
    private void loadImageUrl() {
        if (CommonUtils.ListNotNull(base64Url) && base64Url.size() == releaseUtils.getLocalPath(selectList).size()) {
            loadImageUrl(-1);
        }
    }

    /**
     * 递归上传图片(单张上传)
     *
     * @param position
     */
    private void loadImageUrl(int position) {
        int i = position;
        if (i >= base64Url.size() - 1) {
            releaseContent();
            return;
        }
        Base64UrlData base64UrlData = base64Url.get(++i);
        if (base64UrlData != null) {
            int finalPosition = i;
            new HeadlineImpl(new MarkSixNetCallBack<ReleaseLoadImage>(this, ReleaseLoadImage.class) {
                @Override
                public void onSuccess(ReleaseLoadImage response, int id) {
                    if (response != null) {
                        //设置图片url,清除base64
                        base64UrlData.setImageUrl(response.getThumb());
                        base64UrlData.setBase64("");
                        loadImageUrl(finalPosition);
                    }
                }
            }.setNeedDialog(false)).imagesUploading(base64UrlData.getBase64());
        }
    }

    /**
     * 发布
     */
    private void releaseContent() {
        List<String> list = releaseUtils.getBase64Urls(base64Url);
        if (type == 1) {//编辑
            List<String> localUrls = releaseUtils.getLocalUrls(netPic);
            if (CommonUtils.ListNotNull(localUrls)) {
                list.addAll(0, localUrls);
            }
        }
        new CommArrayImpl(new ArrayCallback<String>(this) {
            @Override
            public void onSuccess(ResultData<String> response) {
                if (type == 1) {//修改
                    toast("修改成功");
                    EventBusUtil.post(new DeleteInvitationEvent());
                } else {
                    toast("发布成功");
                    EventBusUtil.post(new MainClickIndexEvent(1));
                }
                if (CommonUtils.ListNotNull(base64Url)) {
                    base64Url.clear();
                    PictureFileUtils.deleteExternalCacheDirFile(ForumReleaseActivity.this);
                }
                finish();
            }
        }).forumRelease(list, title, content, type == 1 ? id : "");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //包括裁剪和压缩后的缓存，要在上传成功后调用，注意：需要系统sd卡权限
        if (CommonUtils.ListNotNull(selectList)) {
            selectList.clear();
            PictureFileUtils.deleteExternalCacheDirFile(this);
        }
    }

    /**
     * 添加图片
     */
    @Override
    public void onAddPicClick() {
        releaseUtils.setPermission(this, this);
    }

    /**
     * 点击已选图片
     *
     * @param position
     * @param v
     */
    @Override
    public void onItemClick(int position, View v) {
        releaseUtils.setItemClick(this, selectList, position);
    }

    /**
     * 删除
     *
     * @param position
     */
    @Override
    public void moveItem(int position) {
        if (position != RecyclerView.NO_POSITION) {
            if (CommonUtils.ListNotNull(netPic) && position < netPic.size()) {
                netPic.remove(position);
            }
            selectList.remove(position);
            adapter.notifyItemRemoved(position);
            adapter.notifyItemRangeChanged(position, selectList.size());
        }
    }

    @Override
    public void succeedCallBack(@Nullable Object o) {
        PictureFileUtils.deleteCacheDirFile(this);
        if (type == 1) {
            releaseUtils.setMaxSelectNum(releaseUtils.maxSelectNumber - selectList.size());
            if (CommonUtils.ListNotNull(netPic) && CommonUtils.ListNotNull(selectList)) {
                selectList = selectList.subList(netPic.size(), selectList.size());
                isMoved = true;
            }
        }
        releaseUtils.setConfig(this, selectList);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            if (CommonUtils.ListNotNull(netPic) && isMoved && type == 1) {
                isMoved = false;
                selectList.addAll(0, netPic);
                adapter.setList(selectList);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        title = editTitle.getText().toString().trim();
        content = editContent.getText().toString().trim();
        if (CommonUtils.StringNotNull(title)) {
            editTitle.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            editTitle.setTypeface(Typeface.DEFAULT);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_function:
                if (!CommonUtils.StringNotNull(content)) {
                    toast("请输入内容");
                    return;
                }
                release();
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
        if ((view.getId() == R.id.edit_content && CommonUtils.canVerticalScroll(editContent))) {
            view.getParent().requestDisallowInterceptTouchEvent(true);
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }
        }
        return false;
    }
}
