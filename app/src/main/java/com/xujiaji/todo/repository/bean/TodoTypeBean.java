package com.xujiaji.todo.repository.bean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;
import com.xujiaji.todo.module.main.TodoAdapter;

import java.util.List;

/**
 * author: xujiaji
 * created on: 2018/10/10 10:00
 * description:
 */
public class TodoTypeBean extends AbstractExpandableItem<TodoTypeBean.TodoListBean> implements MultiItemEntity {

    /**
     * doneList : [{"date":1533225600000,"todoList":[{"completeDate":1538841600000,"completeDateStr":"2018-10-07","content":"记得睡觉","date":1533225600000,"dateStr":"2018-08-03","id":57,"status":1,"title":"记录","type":0,"userId":5603}]}]
     * todoList : [{"date":1538841600000,"todoList":[{"completeDate":null,"completeDateStr":"","content":"","date":1538841600000,"dateStr":"2018-10-07","id":2852,"status":0,"title":"第二条清单","type":0,"userId":5603}]},{"date":1539792000000,"todoList":[{"completeDate":null,"completeDateStr":"","content":"","date":1539792000000,"dateStr":"2018-10-18","id":2853,"status":0,"title":"第三条清单","type":0,"userId":5603}]}]
     * type : 0
     */

    @SerializedName("type")
    private int type;
    private String title;
    @SerializedName("doneList")
    private List<TodoListBean> doneList;
    @SerializedName("todoList")
    private List<TodoListBean> todoList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<TodoListBean> getDoneList() {
        return doneList;
    }

    public void setDoneList(List<TodoListBean> doneList) {
        this.doneList = doneList;
    }

    public List<TodoListBean> getTodoList() {
        return todoList;
    }

    public void setTodoList(List<TodoListBean> todoList) {
        this.todoList = todoList;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getItemType() {
        return TodoAdapter.TYPE_TODO_TYPE;
    }

    public static class TodoListBean  extends AbstractExpandableItem<TodoTypeBean.TodoListBean.TodoBean> implements MultiItemEntity {
        /**
         * date : 1538841600000
         * todoList : [{"completeDate":null,"completeDateStr":"","content":"","date":1538841600000,"dateStr":"2018-10-07","id":2852,"status":0,"title":"第二条清单","type":0,"userId":5603}]
         */

        @SerializedName("date")
        private long date;
        @SerializedName("todoList")
        private List<TodoBean> todoList;

        public long getDate() {
            return date;
        }

        public void setDate(long date) {
            this.date = date;
        }

        public List<TodoBean> getTodoList() {
            return todoList;
        }

        public void setTodoList(List<TodoBean> todoList) {
            this.todoList = todoList;
        }

        @Override
        public int getItemType() {
            return TodoAdapter.TYPE_TODO_DATE;
        }

        @Override
        public int getLevel() {
            return 0;
        }

        public static class TodoBean implements MultiItemEntity {
            /**
             * completeDate : null
             * completeDateStr :
             * content :
             * date : 1538841600000
             * dateStr : 2018-10-07
             * id : 2852
             * status : 0
             * title : 第二条清单
             * type : 0
             * userId : 5603
             */

            @SerializedName("completeDate")
            private long completeDate;
            @SerializedName("completeDateStr")
            private String completeDateStr;
            @SerializedName("content")
            private String content;
            @SerializedName("date")
            private long date;
            @SerializedName("dateStr")
            private String dateStr;
            @SerializedName("id")
            private int id;
            @SerializedName("status")
            private int status;
            @SerializedName("title")
            private String title;
            @SerializedName("type")
            private int type;
            @SerializedName("userId")
            private int userId;

            public long getCompleteDate() {
                return completeDate;
            }

            public void setCompleteDate(long completeDate) {
                this.completeDate = completeDate;
            }

            public String getCompleteDateStr() {
                return completeDateStr;
            }

            public void setCompleteDateStr(String completeDateStr) {
                this.completeDateStr = completeDateStr;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public long getDate() {
                return date;
            }

            public void setDate(long date) {
                this.date = date;
            }

            public String getDateStr() {
                return dateStr;
            }

            public void setDateStr(String dateStr) {
                this.dateStr = dateStr;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            @Override
            public int getItemType() {
                return TodoAdapter.TYPE_TODO;
            }
        }
    }
}
