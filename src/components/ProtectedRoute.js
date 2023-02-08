import React from "react";
import {Navigate, Route} from 'react-router-dom';

function ProtectedRoute({component: Component, ...restOfProps}){
    const isAuthenticaed=localStorage.getItem("isAuthenticated");
    return(
        <Route
        {...restOfProps}
        render={(props)=>
        isAuthenticaed ? <Component {...props}/> : <Navigate to="/login"/>}
        />
    )
}

export default ProtectedRoute;