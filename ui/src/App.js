import React, { useState, useEffect, useRef } from "react";

function LiveCanvas({ wsUrl, format = "jpeg", start }) {
  const canvasRef = useRef(null);
  const wsRef = useRef(null);

  useEffect(() => {
    if (!start || wsRef.current) return;

    const ws = new WebSocket(wsUrl);
    wsRef.current = ws;

    ws.onopen = () => console.log("WebSocket connected");
    ws.onerror = (err) => console.error("WebSocket error", err);
    ws.onclose = () => console.log("WebSocket closed");

    ws.onmessage = (event) => {
      const img = new Image();
      img.src = `data:image/${format};base64,${event.data}`;

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

    return () => {
      ws.close();
      wsRef.current = null;
    };
  }, [start, wsUrl, format]);

  return (
    <canvas
      ref={canvasRef}
      width={1920}
      height={1080}
      style={{
        width: "100%",
        height: "auto",
        border: "1px solid #ccc",
        marginTop: 20,
        backgroundColor: "#000",
        display: "block",
      }}
    />
  );
}

export default function App() {
  const [startCasting, setStartCasting] = useState(false);
  const [running, setRunning] = useState(false);

  const runTest = async () => {
    if (running) return;

    setRunning(true);
    setStartCasting(true); // ✅ Start screencast immediately

    try {
      await fetch(
        "https://test-runner-slave-kallepallirupesh-dev.apps.rm2.thpm.p1.openshiftapps.com/api/run-test",
        { method: "GET" }
      );
    } catch (err) {
      console.error("Failed to run test", err);
    }
  };

  return (
    <div style={{ padding: 20, maxWidth: 1200, margin: "0 auto" }}>
      <h2>No-Code AI Test Runner</h2>

      <button
        onClick={runTest}
        disabled={running}
        style={{ padding: "8px 16px", fontSize: 16 }}
      >
        {running ? "Running..." : "Run Test"}
      </button>

      <div style={{ marginTop: 40 }}>
        <h3>Live Screencast</h3>
        <LiveCanvas
          wsUrl="wss://test-runner-slave-kallepallirupesh-dev.apps.rm2.thpm.p1.openshiftapps.com/ws/screencast"
          format="jpeg"
          start={startCasting}
        />
      </div>
    </div>
  );
}
