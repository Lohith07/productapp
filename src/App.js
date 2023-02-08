import './App.css';
import {BrowserRouter,Route, Routes} from 'react-router-dom';
import Login from './componenets/Login/Login';
import { useEffect } from 'react';
import Dashboard from './componenets/Dashboard/SupplierDashboard'

function App() {
  return (
    <div className="App">
      <BrowserRouter>
      <Routes>
      <Route exact path="/" component={Login} />
      <Route exact path="/login" element={<Login/>} />
      <Route exact path="/dashboard" component={Dashboard} />
      </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
