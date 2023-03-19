import { useEffect, useContext } from 'react';
import Header from '../common/Header';
import Context from '../../context';

const Chat = () => {
  const { ws } = useContext(Context);

  useEffect(() => {
    document.body.style = 'background: #fff';
    return () => {
      document.body.style = 'background: #fafafa';
    }
  }, []);

  return (
    <div>
      <div id="header">
        <Header />
      </div>
      <div style={{ width: '60.9375rem', height: '100vh', margin: '0 auto', paddingTop: '3.5625rem' }}>
      </div>
    </div>
  );
};
export default Chat;