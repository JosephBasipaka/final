import './App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Customer from './Customer/Customer';
import NavBar from './Navbar/Navbar';
import Dunning from './Dunning/Dunning';
import Payment from './Payment/Payment';
import Add from './Add/Add';
import SuccessPage from './Customer/SuccesPage';

function App() {
  return (
    <div>
      <header>
        <Router>
        <NavBar />
        <Routes>
          <Route path="/" element={<Customer />} />
          <Route path="/add" element={<Add />} />
          <Route path="/dunning" element={<Dunning />} />
          <Route path="/payments" element={<Payment />} />
          <Route path="/success" element={<SuccessPage />} />
          <Route path="*" element = {<div className='max-w-lg mx-auto mt-36 font-sans font-semibold text-sm text-black '> Sorry! Page not Found</div>} />
        </Routes>
        </Router>
      </header>
    </div>

  );
}

export default App;
