
package com.wip.constant;

/**
 *
 */
public interface ErrorConstant {

    interface Common {
        static final String PARAM_IS_EMPTY = "Params Null";
        static final String INVALID_PARAM = "Params Invalid";
        static final String CAN_NOT_FIND_PARAM_TO_CONTIUNE = "Parameter not found.";
    }

    interface Article {
        static final String UPDATE_ARTICLE_FAIL = "Update Fail";
        static final String ADD_NEW_ARTICLE_FAIL = "Add Fail";
        static final String DELETE_ARTICLE_ERROR = "Delete Fail";
        static final String TITLE_IS_TOO_LONG = "Title to Long";
        static final String TITLE_CAN_NOT_EMPTY = "Title is null";
        static final String CONTENT_CAN_NOT_EMPTY = "Content is null";
        static final String CONTENT_IS_TOO_LONG = "The number of words in the article exceeds the limit";

    }

    interface Att {
        static final String ADD_NEW_ATT_FAIL = "Add File Fail";
        static final String UPDATE_ATT_FAIL =  "Update File Fail";
        static final String DELETE_ATT_FAIL = "Delete File Fail";
        static final String UPLOAD_FILE_FAIL = "Upload File Fail";
    }

    interface Comment {
        static final String ADD_NEW_COMMENT_FAIL = "Failed to add comment";
        static final String UPDATE_COMMENT_FAIL = "Failed to update comment";
        static final String DELETE_COMMENT_FAIL = "Failed to delete comment";
        static final String COMMENT_NOT_EXIST = "Comment does not exist";
    }

    interface Option {
        static final String DELETE_OPTION_FAIL = "Failed to delete configuration";
        static final String UPDATE_OPTION_FAIL = "Failed to uodate configuration";
    }

    interface Meta {
        static final String ADD_META_FAIL = "Failed to add project information";
        static final String UPDATE_META_FAIL = "Failed to update project information";
        static final String DELETE_META_FAIL = "Failed to delete project information";
        static final String NOT_ONE_RESULT = "More than one item was obtained";
        static final String META_IS_EXIST = "The project already exists";
    }

    interface Auth {
        static final String USERNAME_PASSWORD_IS_EMPTY = "User name and password cannot be empty";
        static final String USERNAME_PASSWORD_ERROR = "The user name does not exist or the password is incorrect";
        static final String NOT_LOGIN = "User not logged in";
    }

}
