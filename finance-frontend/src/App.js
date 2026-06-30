import { BrowserRouter, Routes, Route, Navigate} from "react-router-dom";
import HomePage from "./auth/HomePage";
import Dashboard from "./dash/Dashboard";
import LoginPage from "./auth/LoginPage";
import RegisterPage from "./auth/RegisterPage";
import VerifyPage from "./auth/VerifyPage";
function App(){

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/auth" replace />} />
        <Route path="/auth" element={<HomePage/>} />
        <Route path="/auth/login" element={<LoginPage/>} />
        <Route path="/auth/register" element={<RegisterPage/>} />
        <Route path="/auth/verify" element={<VerifyPage/>} />
        <Route path="/dashboard" element={<Dashboard />} />
      </Routes>
    </BrowserRouter>
  );
}
export default App;
