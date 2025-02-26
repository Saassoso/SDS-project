import net from "net";
import { Server } from "socket.io"; 

const TCP_PORT = 5001;
const WS_PORT = 3001;

const io = new Server(WS_PORT, {
  cors: {
    origin: ["http://localhost:3000", "http://localhost:5173"], 
    methods: ["GET", "POST"]
  }
});

console.log(`Socket.IO server running on ws://localhost:${WS_PORT}`);

io.on("connection", (socket) => {
  console.log("React client connected via Socket.IO");

  socket.on("disconnect", () => {
    console.log("React client disconnected");
  });
});

const server = net.createServer((socket) => {
  console.log("Connected to Java TCP Server");

  socket.on("data", (data) => {
    const message = data.toString();
    console.log("Received alert:", message);

    io.emit("kafka-error", message);
  });

  socket.on("close", () => console.log("Java TCP server disconnected"));
});

server.listen(TCP_PORT, () => {
  console.log(`TCP server listening on port ${TCP_PORT}`);
});
