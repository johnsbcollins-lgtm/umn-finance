import { BrowserRouter, Routes, Route, Navigate} from "react-router";
import HomePage from "./auth/HomePage";
import Dashboard from "./dash/Dashboard";
import LoginPage from "./auth/LoginPage";
import RegisterPage from "./auth/RegisterPage";
function App(){
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/auth" element={<HomePage/>} />
        <Route path="/auth/login" element={<LoginPage/>} />
        <Route path="/auth/register" element={<RegisterPage/>} />
        <Route path="/dashboard" element={<Dashboard />} />
      </Routes>
    </BrowserRouter>
  );
}
export default App;
