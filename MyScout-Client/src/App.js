import { useState, useEffect } from 'react';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import Login from './components/login/Login';
import Home from './components/home/Home';
import Share from './components/post/Share';
import Profile from './components/profile/Profile';
import Chat from './components/chat/Chat';
import Loading from './components/common/Loading';
import PrivateRoute from './components/common/PrivateRoute';
import Context from './context';
import './index.css';

function App() {
  const [isLoading, setIsLoading] = useState(false);
  const [user, setUser] = useState(null);
  const [stomp, setstomp] = useState(null);
  const [hasNewPost, setHasNewPost] = useState(false);
  const [selectedPost, setSelectedPost] = useState(false);

  let listenCustomMessages = null;

  useEffect(() => {
    initAuthUser();
    initstomp();
  }, []);

  useEffect(() => {
    if (stomp && user) { 
      listenCustomMessages();
    }
    return () => {
      if (stomp && user) {
        stomp.removeMessageListener(user.id);
      }
    }
  }, [stomp, user, listenCustomMessages]);

  const initAuthUser = () => {
    const authenticatedUser = localStorage.getItem('auth');
    if (authenticatedUser) {
      setUser(JSON.parse(authenticatedUser));
    }
  };

  const initstomp = async () => {
    const appID = `${process.env.REACT_APP_stomp_APP_ID}`;
    const region = `${process.env.REACT_APP_stomp_REGION}`;
    const appSetting = new stomp.AppSettingsBuilder().subscribePresenceForAllUsers().setRegion(region).build();
    stomp.init(appID, appSetting).then(
      () => {
        setstomp(() => stomp);
      },
      error => {
      }
    );
  };

  listenCustomMessages = () => {
    stomp.addMessageListener(
      user.id,
      new stomp.MessageListener({
        onCustomMessageReceived: customMessage => {
          if (customMessage && customMessage.sender && customMessage.sender.uid && customMessage.sender.uid !== user.id && customMessage.data && customMessage.data.customData && customMessage.data.customData.message) {
            if (customMessage && customMessage.type && customMessage.type === 'notification') {
              alert(customMessage.data.customData.message);
            }
          }
        }
      })
    );
  };

  return (
    <Context.Provider value={{ isLoading, setIsLoading, user, setUser, stomp, hasNewPost, setHasNewPost, selectedPost, setSelectedPost }}>
      <Router>
        <Switch>
          <PrivateRoute exact path="/" component={Home} />
          <PrivateRoute exact path="/posts/:id" component={Share} />
          <PrivateRoute exact path="/users/:id" component={Profile} />
          <PrivateRoute exact path="/chat" component={Chat} />
          <Route exact path="/login">
            <Login />
          </Route>
        </Switch>
      </Router>
      {isLoading && <Loading />}
    </Context.Provider>
  );
}

export default App;
