package com.example.appbella.Database;

import android.database.Cursor;
import androidx.room.EmptyResultSetException;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.RxRoom;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.lang.Void;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class CartDAO_Impl implements CartDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CartItem> __insertionAdapterOfCartItem;

  private final EntityDeletionOrUpdateAdapter<CartItem> __deletionAdapterOfCartItem;

  private final EntityDeletionOrUpdateAdapter<CartItem> __updateAdapterOfCartItem;

  private final SharedSQLiteStatement __preparedStmtOfCleanCart;

  public CartDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCartItem = new EntityInsertionAdapter<CartItem>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `Cart` (`productId`,`productName`,`productImage`,`productPrice`,`productQuantity`,`userPhone`,`categoryId`,`productAddon`,`productSize`,`productExtraPrice`,`fbid`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, CartItem value) {
        if (value.getProductId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getProductId());
        }
        if (value.getProductName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getProductName());
        }
        if (value.getProductImage() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getProductImage());
        }
        if (value.getProductPrice() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindLong(4, value.getProductPrice());
        }
        stmt.bindLong(5, value.getProductQuantity());
        if (value.getUserPhone() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getUserPhone());
        }
        if (value.getCategoryId() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getCategoryId());
        }
        if (value.getProductAddon() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getProductAddon());
        }
        if (value.getProductSize() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getProductSize());
        }
        if (value.getProductExtraPrice() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindLong(10, value.getProductExtraPrice());
        }
        if (value.getFbid() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getFbid());
        }
      }
    };
    this.__deletionAdapterOfCartItem = new EntityDeletionOrUpdateAdapter<CartItem>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `Cart` WHERE `productId` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, CartItem value) {
        if (value.getProductId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getProductId());
        }
      }
    };
    this.__updateAdapterOfCartItem = new EntityDeletionOrUpdateAdapter<CartItem>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR REPLACE `Cart` SET `productId` = ?,`productName` = ?,`productImage` = ?,`productPrice` = ?,`productQuantity` = ?,`userPhone` = ?,`categoryId` = ?,`productAddon` = ?,`productSize` = ?,`productExtraPrice` = ?,`fbid` = ? WHERE `productId` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, CartItem value) {
        if (value.getProductId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getProductId());
        }
        if (value.getProductName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getProductName());
        }
        if (value.getProductImage() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getProductImage());
        }
        if (value.getProductPrice() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindLong(4, value.getProductPrice());
        }
        stmt.bindLong(5, value.getProductQuantity());
        if (value.getUserPhone() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getUserPhone());
        }
        if (value.getCategoryId() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getCategoryId());
        }
        if (value.getProductAddon() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getProductAddon());
        }
        if (value.getProductSize() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getProductSize());
        }
        if (value.getProductExtraPrice() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindLong(10, value.getProductExtraPrice());
        }
        if (value.getFbid() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getFbid());
        }
        if (value.getProductId() == null) {
          stmt.bindNull(12);
        } else {
          stmt.bindString(12, value.getProductId());
        }
      }
    };
    this.__preparedStmtOfCleanCart = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM Cart WHERE userPhone=?";
        return _query;
      }
    };
  }

  @Override
  public Completable insertOrReplaceAll(final CartItem... cartItems) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCartItem.insert(cartItems);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public Single<Integer> deleteCart(final CartItem cart) {
    return Single.fromCallable(new Callable<Integer>() {
      @Override
      public Integer call() throws Exception {
        int _total = 0;
        __db.beginTransaction();
        try {
          _total +=__deletionAdapterOfCartItem.handle(cart);
          __db.setTransactionSuccessful();
          return _total;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public Single<Integer> updateCart(final CartItem cart) {
    return Single.fromCallable(new Callable<Integer>() {
      @Override
      public Integer call() throws Exception {
        int _total = 0;
        __db.beginTransaction();
        try {
          _total +=__updateAdapterOfCartItem.handle(cart);
          __db.setTransactionSuccessful();
          return _total;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public Single<Integer> cleanCart(final String userPhone) {
    return Single.fromCallable(new Callable<Integer>() {
      @Override
      public Integer call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfCleanCart.acquire();
        int _argIndex = 1;
        if (userPhone == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, userPhone);
        }
        __db.beginTransaction();
        try {
          final java.lang.Integer _result = _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
          __preparedStmtOfCleanCart.release(_stmt);
        }
      }
    });
  }

  @Override
  public Flowable<List<CartItem>> getAllCart(final String userPhone) {
    final String _sql = "SELECT * FROM Cart WHERE userPhone=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (userPhone == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, userPhone);
    }
    return RxRoom.createFlowable(__db, false, new String[]{"Cart"}, new Callable<List<CartItem>>() {
      @Override
      public List<CartItem> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfProductId = CursorUtil.getColumnIndexOrThrow(_cursor, "productId");
          final int _cursorIndexOfProductName = CursorUtil.getColumnIndexOrThrow(_cursor, "productName");
          final int _cursorIndexOfProductImage = CursorUtil.getColumnIndexOrThrow(_cursor, "productImage");
          final int _cursorIndexOfProductPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "productPrice");
          final int _cursorIndexOfProductQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "productQuantity");
          final int _cursorIndexOfUserPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "userPhone");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfProductAddon = CursorUtil.getColumnIndexOrThrow(_cursor, "productAddon");
          final int _cursorIndexOfProductSize = CursorUtil.getColumnIndexOrThrow(_cursor, "productSize");
          final int _cursorIndexOfProductExtraPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "productExtraPrice");
          final int _cursorIndexOfFbid = CursorUtil.getColumnIndexOrThrow(_cursor, "fbid");
          final List<CartItem> _result = new ArrayList<CartItem>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final CartItem _item;
            _item = new CartItem();
            final String _tmpProductId;
            _tmpProductId = _cursor.getString(_cursorIndexOfProductId);
            _item.setProductId(_tmpProductId);
            final String _tmpProductName;
            _tmpProductName = _cursor.getString(_cursorIndexOfProductName);
            _item.setProductName(_tmpProductName);
            final String _tmpProductImage;
            _tmpProductImage = _cursor.getString(_cursorIndexOfProductImage);
            _item.setProductImage(_tmpProductImage);
            final Long _tmpProductPrice;
            if (_cursor.isNull(_cursorIndexOfProductPrice)) {
              _tmpProductPrice = null;
            } else {
              _tmpProductPrice = _cursor.getLong(_cursorIndexOfProductPrice);
            }
            _item.setProductPrice(_tmpProductPrice);
            final int _tmpProductQuantity;
            _tmpProductQuantity = _cursor.getInt(_cursorIndexOfProductQuantity);
            _item.setProductQuantity(_tmpProductQuantity);
            final String _tmpUserPhone;
            _tmpUserPhone = _cursor.getString(_cursorIndexOfUserPhone);
            _item.setUserPhone(_tmpUserPhone);
            final String _tmpCategoryId;
            _tmpCategoryId = _cursor.getString(_cursorIndexOfCategoryId);
            _item.setCategoryId(_tmpCategoryId);
            final String _tmpProductAddon;
            _tmpProductAddon = _cursor.getString(_cursorIndexOfProductAddon);
            _item.setProductAddon(_tmpProductAddon);
            final String _tmpProductSize;
            _tmpProductSize = _cursor.getString(_cursorIndexOfProductSize);
            _item.setProductSize(_tmpProductSize);
            final Long _tmpProductExtraPrice;
            if (_cursor.isNull(_cursorIndexOfProductExtraPrice)) {
              _tmpProductExtraPrice = null;
            } else {
              _tmpProductExtraPrice = _cursor.getLong(_cursorIndexOfProductExtraPrice);
            }
            _item.setProductExtraPrice(_tmpProductExtraPrice);
            final String _tmpFbid;
            _tmpFbid = _cursor.getString(_cursorIndexOfFbid);
            _item.setFbid(_tmpFbid);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Single<Integer> countItemInCart(final String userPhone) {
    final String _sql = "SELECT COUNT(*) FROM Cart WHERE userPhone=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (userPhone == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, userPhone);
    }
    return RxRoom.createSingle(new Callable<Integer>() {
      @Override
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if(_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          if(_result == null) {
            throw new EmptyResultSetException("Query returned empty result set: " + _statement.getSql());
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Single<Long> sumPrice(final String userPhone) {
    final String _sql = "SELECT SUM(productPrice*productQuantity) FROM Cart WHERE userPhone=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (userPhone == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, userPhone);
    }
    return RxRoom.createSingle(new Callable<Long>() {
      @Override
      public Long call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Long _result;
          if(_cursor.moveToFirst()) {
            final Long _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          if(_result == null) {
            throw new EmptyResultSetException("Query returned empty result set: " + _statement.getSql());
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Single<Long> sumQuantity(final String userPhone) {
    final String _sql = "SELECT SUM(productQuantity) FROM Cart WHERE userPhone=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (userPhone == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, userPhone);
    }
    return RxRoom.createSingle(new Callable<Long>() {
      @Override
      public Long call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Long _result;
          if(_cursor.moveToFirst()) {
            final Long _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          if(_result == null) {
            throw new EmptyResultSetException("Query returned empty result set: " + _statement.getSql());
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flowable<CartItem> getItemInCart(final String productId, final String categoryId,
      final String userPhone) {
    final String _sql = "SELECT * FROM Cart WHERE  productId=? AND categoryId=? AND userPhone=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    if (productId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, productId);
    }
    _argIndex = 2;
    if (categoryId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, categoryId);
    }
    _argIndex = 3;
    if (userPhone == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, userPhone);
    }
    return RxRoom.createFlowable(__db, false, new String[]{"Cart"}, new Callable<CartItem>() {
      @Override
      public CartItem call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfProductId = CursorUtil.getColumnIndexOrThrow(_cursor, "productId");
          final int _cursorIndexOfProductName = CursorUtil.getColumnIndexOrThrow(_cursor, "productName");
          final int _cursorIndexOfProductImage = CursorUtil.getColumnIndexOrThrow(_cursor, "productImage");
          final int _cursorIndexOfProductPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "productPrice");
          final int _cursorIndexOfProductQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "productQuantity");
          final int _cursorIndexOfUserPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "userPhone");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfProductAddon = CursorUtil.getColumnIndexOrThrow(_cursor, "productAddon");
          final int _cursorIndexOfProductSize = CursorUtil.getColumnIndexOrThrow(_cursor, "productSize");
          final int _cursorIndexOfProductExtraPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "productExtraPrice");
          final int _cursorIndexOfFbid = CursorUtil.getColumnIndexOrThrow(_cursor, "fbid");
          final CartItem _result;
          if(_cursor.moveToFirst()) {
            _result = new CartItem();
            final String _tmpProductId;
            _tmpProductId = _cursor.getString(_cursorIndexOfProductId);
            _result.setProductId(_tmpProductId);
            final String _tmpProductName;
            _tmpProductName = _cursor.getString(_cursorIndexOfProductName);
            _result.setProductName(_tmpProductName);
            final String _tmpProductImage;
            _tmpProductImage = _cursor.getString(_cursorIndexOfProductImage);
            _result.setProductImage(_tmpProductImage);
            final Long _tmpProductPrice;
            if (_cursor.isNull(_cursorIndexOfProductPrice)) {
              _tmpProductPrice = null;
            } else {
              _tmpProductPrice = _cursor.getLong(_cursorIndexOfProductPrice);
            }
            _result.setProductPrice(_tmpProductPrice);
            final int _tmpProductQuantity;
            _tmpProductQuantity = _cursor.getInt(_cursorIndexOfProductQuantity);
            _result.setProductQuantity(_tmpProductQuantity);
            final String _tmpUserPhone;
            _tmpUserPhone = _cursor.getString(_cursorIndexOfUserPhone);
            _result.setUserPhone(_tmpUserPhone);
            final String _tmpCategoryId;
            _tmpCategoryId = _cursor.getString(_cursorIndexOfCategoryId);
            _result.setCategoryId(_tmpCategoryId);
            final String _tmpProductAddon;
            _tmpProductAddon = _cursor.getString(_cursorIndexOfProductAddon);
            _result.setProductAddon(_tmpProductAddon);
            final String _tmpProductSize;
            _tmpProductSize = _cursor.getString(_cursorIndexOfProductSize);
            _result.setProductSize(_tmpProductSize);
            final Long _tmpProductExtraPrice;
            if (_cursor.isNull(_cursorIndexOfProductExtraPrice)) {
              _tmpProductExtraPrice = null;
            } else {
              _tmpProductExtraPrice = _cursor.getLong(_cursorIndexOfProductExtraPrice);
            }
            _result.setProductExtraPrice(_tmpProductExtraPrice);
            final String _tmpFbid;
            _tmpFbid = _cursor.getString(_cursorIndexOfFbid);
            _result.setFbid(_tmpFbid);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }
}
