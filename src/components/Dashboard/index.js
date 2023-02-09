import { useEffect, useState } from "react";
import SupplierDashboard from './SupplierDashboard'
import ManufacturerDashboard from './ManufacturerDashboard';
import WholesalerDashboard from './WholesalerDashboard';
import Login from "../Login/Login";
import axios from "axios";

export default function Dashboard(props) {
    const [user, setUser] = useState('');
    const {serverport}=props;
    const currentuser = localStorage.getItem("currentuser");
    console.log(serverport);
    const url = "http://localhost:"+serverport;
    console.log(url);

    async function getcurrenthost() {
        
        console.log(currentuser);
        const res = await axios.get(url+serverport+"/currentuser");
        console.log(res.data);
        Login.SpinnerControl(true);
        
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
