package com.zdzc.dataClear.entity;

/**
 *  * @Description: 序列号属性
 * 如轨迹序列号为：1,000,978，head为1，表示库序号，tail为为000978，表示第多少辆车，后6位，每库10万辆车，seqNo为1,000,978
 * 如报警序列号为：13,000,000，head为1，表示库序号，tail为为3000000，表示第多少辆车，后7位，每库300万辆车，seqNo为13,000,000
 *  * @author chengwengao
 *  * @date 2017/12/6 0006 16:42
 *  
 */
public class SeqNoAttr {
    private Long head;   //头部，库序号
    private Long tail;  //尾部，标志第多少条数据
    private Long seqNo; //带库信息与序号的完整序列号
    private boolean pushAlarm;  //是否推送报警
    private String msg; //消息

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isPushAlarm() {
        return pushAlarm;
    }

    public void setPushAlarm(boolean pushAlarm) {
        this.pushAlarm = pushAlarm;
    }

    public Long getHead() {
        return head;
    }

    public void setHead(Long head) {
        this.head = head;
    }

    public Long getTail() {
        return tail;
    }

    public void setTail(Long tail) {
        this.tail = tail;
    }

    public Long getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Long seqNo) {
        this.seqNo = seqNo;
    }

    public SeqNoAttr() {
    }
}
