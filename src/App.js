import './App.css';
import {BrowserRouter,Route, Routes} from 'react-router-dom';
import Login from './components/Login/Login';
import { useEffect } from 'react';
import Dashboard from './components/Dashboard'
import AllProducts from './components/AllProducts';

function App() {
  return (
    <div className="App">
      <BrowserRouter>
      <Routes>
      <Route exact path="/" element={<Login />} />
      <Route exact path="/login" element={<Login />} />
      <Route exact path="/dashboard" element={<Dashboard />} />
      <Route exact path="/all-products" element={<AllProducts />} />
      </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
