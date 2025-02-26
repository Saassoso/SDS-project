import { useState, useEffect } from "react";
import axios from "axios";
import "../assets/css/Dashboard.css"; 
import { io } from "socket.io-client";
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from "recharts";

function Dashboard() {
  const [sensorData, setSensorData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [kafkaError, setKafkaError] = useState("");

 
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get("http://localhost:9090/server-auth/webapi/sensors/fieldData");
        setSensorData(response.data);
        setLoading(false);
      } catch (err) {
        setError("Failed to fetch sensor data");
        setLoading(false);
      }
    };

    fetchData();
    const interval = setInterval(fetchData, 300000);
    return () => clearInterval(interval);
  }, []);

  useEffect(() => {
    const socket = io("http://localhost:3001"); 
  
    socket.on("connect", () => {
      console.log("Connected to Socket.IO Server");
    });
  
    socket.on("kafka-error", (message) => {
      console.log("Received Kafka Error:", message);
      setKafkaError(message);
  
      // Remove popup after 5 seconds
      setTimeout(() => setKafkaError(""), 5000);
    });
  
    socket.on("disconnect", () => {
      console.log("Disconnected from Socket.IO Server");
    });
  
    return () => {
      socket.disconnect();
    };
  }, []);

  if (loading) return <div className="text-center mt-8">Loading...</div>;
  if (error) return <div className="text-center mt-8 text-red-500">{error}</div>;

  return (
    <div className="dashboard-container">
      <h1>Field Sensor Dashboard</h1>

      {kafkaError && <div className="popup">⚠️ {kafkaError}</div>}

      <div className="grid-container">
        {/* Temperature Chart */}
        <div className="chart-container">
          <h2 className="chart-title">Temperature</h2>
          <ResponsiveContainer width="100%" height={300}>
            <LineChart data={sensorData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="timestamp" />
              <YAxis />
              <Tooltip />
              <Legend />
              <Line type="monotone" dataKey="temperature" stroke="#ff7300" />
            </LineChart>
          </ResponsiveContainer>
        </div>

        {/* pH Chart */}
        <div className="chart-container">
          <h2 className="chart-title">pH Level</h2>
          <ResponsiveContainer width="100%" height={300}>
            <LineChart data={sensorData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="timestamp" />
              <YAxis />
              <Tooltip />
              <Legend />
              <Line type="monotone" dataKey="ph" stroke="#82ca9d" />
            </LineChart>
          </ResponsiveContainer>
        </div>

        {/* Soil Moisture Chart */}
        <div className="chart-container">
          <h2 className="chart-title">Soil Moisture</h2>
          <ResponsiveContainer width="100%" height={300}>
            <LineChart data={sensorData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="timestamp" />
              <YAxis />
              <Tooltip />
              <Legend />
              <Line type="monotone" dataKey="soilmoisture" stroke="#8884d8" />
            </LineChart>
          </ResponsiveContainer>
        </div>

        {/* Humidity Chart */}
        <div className="chart-container">
          <h2 className="chart-title">Humidity</h2>
          <ResponsiveContainer width="100%" height={300}>
            <LineChart data={sensorData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="timestamp" />
              <YAxis />
              <Tooltip />
              <Legend />
              <Line type="monotone" dataKey="humidity" stroke="#ffc658" />
            </LineChart>
          </ResponsiveContainer>
        </div>
      </div>
    </div>
  );
}

export default Dashboard;
