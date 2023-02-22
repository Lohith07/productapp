import { useEffect, useState } from "react";
import SupplierDashboard from './SupplierDashboard'
import ManufacturerDashboard from './ManufacturerDashboard';
import WholesalerDashboard from './WholesalerDashboard';
import Login from "../Login/Login";
import axios from "axios";

export default function Dashboard() {
    const [user, setUser] = useState('');
    const host = localStorage.getItem("host");
    const url="http://"+host+"/currentuser";

    async function getcurrenthost() {
    
        const res = await axios.get(url);
        setUser(res.data.port);
        
    }
    useEffect(() => {
        getcurrenthost();
    }, []);
    return (
        <div>
            {user && (((user === "10006" && <SupplierDashboard />) || (user === "10009" && <ManufacturerDashboard />) || <WholesalerDashboard />))}
        </div>
    )
}
