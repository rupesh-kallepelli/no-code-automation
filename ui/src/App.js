import React, { useEffect, useRef, useState } from "react";

export default function App() {
  const canvasRef = useRef(null);
  const wsRef = useRef(null);

  const [wsUrl, setWsUrl] = useState("");
  const [connected, setConnected] = useState(false);
  const [running, setRunning] = useState(false);

  const connectWebSocket = () => {
    if (!wsUrl || connected) return;

    const ws = new WebSocket(wsUrl);
    wsRef.current = ws;

    ws.onopen = () => {
      console.log("WebSocket connected");
      setConnected(true);
    };

    ws.onerror = (err) => {
      console.error("WebSocket error", err);
    };

    ws.onclose = () => {
      console.log("WebSocket closed");
      setConnected(false);
      wsRef.current = null;
    };

    ws.onmessage = (event) => {
      const img = new Image();
      img.src = `data:image/jpeg;base64,${event.data}`;

      img.onload = () => {
        const canvas = canvasRef.current;
        if (!canvas) return;

        const ctx = canvas.getContext("2d");
        canvas.width = 1920;
        canvas.height = 1080;

        ctx.clearRect(0, 0, canvas.width, canvas.height);
        ctx.drawImage(img, 0, 0, canvas.width, canvas.height);
      };
    };
  };

  const disconnectWebSocket = () => {
    if (wsRef.current) {
      wsRef.current.close();
      wsRef.current = null;
    }
  };

  useEffect(() => {
    return () => {
      disconnectWebSocket();
    };
  }, []);

  const runTest = async () => {
    if (running) return;

    setRunning(true);
    try {
      await fetch(
        "https://test-runner-slave-kallepallirupesh-dev.apps.rm2.thpm.p1.openshiftapps.com/api/run-test",
        { method: "GET" }
      );
    } catch (err) {
      console.error("Failed to run test", err);
    } finally {
      setRunning(false);
    }
  };

  return (
    <div style={{ padding: 20, maxWidth: 1200, margin: "0 auto" }}>
      <h2>No-Code AI Test Runner</h2>

      {/* WebSocket URL Input */}
      <div style={{ marginBottom: 16 }}>
        <input
          type="text"
          placeholder="wss://host/ws/screencast"
          value={wsUrl}
          onChange={(e) => setWsUrl(e.target.value)}
          style={{
            width: "100%",
            padding: 8,
            fontSize: 14,
            marginBottom: 8,
          }}
        />

        {!connected ? (
          <button onClick={connectWebSocket} style={{ padding: "6px 12px" }}>
            Connect WebSocket
          </button>
        ) : (
          <button onClick={disconnectWebSocket} style={{ padding: "6px 12px" }}>
            Disconnect WebSocket
          </button>
        )}
      </div>

      {/* Run Test */}
      <button
        onClick={runTest}
        disabled={running}
        style={{ padding: "8px 16px", fontSize: 16 }}
      >
        {running ? "Running..." : "Run Test"}
      </button>

      {/* Screencast */}
      <div style={{ marginTop: 30 }}>
        <h3>Live Screencast</h3>
        <canvas
          ref={canvasRef}
          width={1920}
          height={1080}
          style={{
            width: "100%",
            height: "auto",
            border: "1px solid #ccc",
            backgroundColor: "#000",
            display: "block",
          }}
        />
      </div>
    </div>
  );
}
