package com.toandoan.roomdatabase;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.toandoan.roomdatabase.data.UserRepository;
import com.toandoan.roomdatabase.data.local.UserDatabase;
import com.toandoan.roomdatabase.data.local.UserLocalDataSource;
import com.toandoan.roomdatabase.data.model.Place;
import com.toandoan.roomdatabase.data.model.User;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity
    implements View.OnClickListener, AdapterView.OnItemLongClickListener,
    AdapterView.OnItemClickListener {

    private CompositeDisposable mCompositeDisposable;
    private ListView mListView;
    private UserRepository mUserRepository;
    private List<User> mUsers;
    private ArrayAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCompositeDisposable = new CompositeDisposable();

        mUsers = new ArrayList<>();
        mListView = findViewById(R.id.list_user);
        mListView.setOnItemLongClickListener(this);
        mListView.setOnItemClickListener(this);
        mAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mUsers);
        mListView.setAdapter(mAdapter);

        UserDatabase userDatabase = UserDatabase.getInstance(this);
        mUserRepository =
            UserRepository.getInstance(UserLocalDataSource.getInstance(userDatabase.userDAO()));

        findViewById(R.id.floatingActionButton).setOnClickListener(this);
        getData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }

    private void getData() {
        Disposable disposable = mUserRepository.getALlUser()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new Consumer<List<User>>() {
                @Override
                public void accept(List<User> users) throws Exception {
                    onGetAllUserSuccess(users);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    onGetAllUserFailure(throwable.getMessage());
                }
            });

        mCompositeDisposable.add(disposable);
    }

    private void onGetAllUserFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void onGetAllUserSuccess(List<User> users) {
        mUsers.clear();
        mUsers.addAll(users);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Clear all user when click at option menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear:
            default:
                deleteAllUser();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllUser() {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                mUserRepository.deleteAllUser();
                e.onComplete();
            }
        })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    //no ops
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    onGetAllUserFailure(throwable.getMessage());
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    getData();
                }
            });

        mCompositeDisposable.add(disposable);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() != R.id.floatingActionButton) {
            return;
        }
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                User user = new User("Toan", " Doan " + new Random().nextInt());
                Place place = new Place("YB " + new Random().nextInt());
                user.setPlace(place);
                mUserRepository.insertUser(user);
                e.onComplete();
            }
        })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    //no ops
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    onGetAllUserFailure(throwable.getMessage());
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    getData();
                }
            });

        mCompositeDisposable.add(disposable);
    }

    /**
     * Edit User Last Name when click at item
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        final User user = mUsers.get(i);
        final EditText editText = new EditText(this);
        editText.setText(user.getLastName());
        editText.setHint(R.string.hint_last_name);
        new AlertDialog.Builder(this).setTitle("Edit")
            .setMessage("Edit user last name")
            .setView(editText)
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (TextUtils.isEmpty(editText.getText().toString())) {
                        return;
                    }
                    user.setLastName(editText.getText().toString());
                    updateUser(user);
                }
            })
            .setNegativeButton(android.R.string.cancel, null)
            .create()
            .show();
    }

    public void updateUser(final User user) {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                mUserRepository.updateUser(user);
                e.onComplete();
            }
        })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    //no ops
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    onGetAllUserFailure(throwable.getMessage());
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    getData();
                }
            });

        mCompositeDisposable.add(disposable);
    }

    /**
     * Confirm delete item when long click
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        final User user = mUsers.get(i);
        new AlertDialog.Builder(this).setTitle("Delete")
            .setMessage("Do you want to delete " + user.toString())
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    deleteUser(user);
                }
            })
            .setNegativeButton(android.R.string.cancel, null)
            .create()
            .show();
        return false;
    }

    private void deleteUser(final User user) {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                mUserRepository.deleteUser(user);
                e.onComplete();
            }
        })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    //no ops
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    onGetAllUserFailure(throwable.getMessage());
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    getData();
                }
            });

        mCompositeDisposable.add(disposable);
    }
}
