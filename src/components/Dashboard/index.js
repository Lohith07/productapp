import { useEffect, useState } from "react";
import SupplierDashboard from './SupplierDashboard'
import ManufacturerDashboard from './ManufacturerDashboard';
import WholesalerDashboard from './WholesalerDashboard';
import axios from "axios";

export default function Dashboard() {
    const [user, setUser] = useState('');
    const url = "http://localhost:50011/currentuser";


    async function getcurrenthost(props) {
        const currentuser = localStorage.getItem("currentuser");
        console.log(currentuser);
        const res = await axios.get(url);
        const host = res.data.host + ":" + res.data.port;
        console.log(host);
        
        if (host == currentuser) {
            setUser(res.data.port);
        }
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
