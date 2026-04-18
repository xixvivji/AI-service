import { useEffect, useState, useTransition } from "react";

const initialForm = {
  customerEmail: "",
  subject: "",
  content: "",
  priority: "MEDIUM"
};

const priorities = ["LOW", "MEDIUM", "HIGH", "URGENT"];

function App() {
  const [form, setForm] = useState(initialForm);
  const [tickets, setTickets] = useState([]);
  const [selectedTicket, setSelectedTicket] = useState(null);
  const [status, setStatus] = useState("Loading tickets...");
  const [error, setError] = useState("");
  const [isPending, startTransition] = useTransition();

  useEffect(() => {
    void loadTickets();
  }, []);

  async function loadTickets() {
    setError("");
    setStatus("Refreshing ticket queue...");

    try {
      const response = await fetch("/api/v1/tickets");
      if (!response.ok) {
        throw new Error("Failed to load tickets");
      }

      const data = await response.json();
      startTransition(() => {
        setTickets(data);
        setSelectedTicket(data[0] ?? null);
      });
      setStatus(`${data.length} ticket(s) loaded`);
    } catch (requestError) {
      setError(requestError.message);
      setStatus("Ticket queue unavailable");
    }
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setError("");
    setStatus("Submitting ticket...");

    try {
      const response = await fetch("/api/v1/tickets", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(form)
      });

      if (!response.ok) {
        const payload = await response.json().catch(() => null);
        throw new Error(payload?.message || "Ticket creation failed");
      }

      const createdTicket = await response.json();
      startTransition(() => {
        setTickets((current) => [createdTicket, ...current]);
        setSelectedTicket(createdTicket);
        setForm(initialForm);
      });
      setStatus(`Ticket #${createdTicket.id} created`);
    } catch (requestError) {
      setError(requestError.message);
      setStatus("Submission failed");
    }
  }

  function updateForm(field, value) {
    setForm((current) => ({
      ...current,
      [field]: value
    }));
  }

  return (
    <div className="app-shell">
      <div className="hero-panel">
        <p className="eyebrow">AI Customer Support Platform</p>
        <h1>Operations console for intake, triage, and AI-assisted response drafts.</h1>
        <p className="hero-copy">
          This frontend talks to the Spring Boot backend and shows the current ticket queue with
          AI categorization and reply drafting.
        </p>
        <div className="status-strip">
          <span>{status}</span>
          {isPending ? <span className="pending-badge">Updating</span> : null}
        </div>
      </div>

      <main className="content-grid">
        <section className="panel">
          <div className="panel-header">
            <div>
              <p className="panel-kicker">Create Ticket</p>
              <h2>New customer inquiry</h2>
            </div>
          </div>

          <form className="ticket-form" onSubmit={handleSubmit}>
            <label>
              Customer email
              <input
                type="email"
                value={form.customerEmail}
                onChange={(event) => updateForm("customerEmail", event.target.value)}
                placeholder="customer@example.com"
                required
              />
            </label>

            <label>
              Subject
              <input
                value={form.subject}
                onChange={(event) => updateForm("subject", event.target.value)}
                placeholder="Duplicate charge refund request"
                maxLength={180}
                required
              />
            </label>

            <label>
              Priority
              <select
                value={form.priority}
                onChange={(event) => updateForm("priority", event.target.value)}
              >
                {priorities.map((priority) => (
                  <option key={priority} value={priority}>
                    {priority}
                  </option>
                ))}
              </select>
            </label>

            <label>
              Inquiry content
              <textarea
                value={form.content}
                onChange={(event) => updateForm("content", event.target.value)}
                placeholder="Describe the issue, urgency, and expected outcome."
                rows={7}
                maxLength={5000}
                required
              />
            </label>

            <button type="submit">Submit ticket</button>
          </form>

          {error ? <p className="error-banner">{error}</p> : null}
        </section>

        <section className="panel queue-panel">
          <div className="panel-header">
            <div>
              <p className="panel-kicker">Ticket Queue</p>
              <h2>Recent inquiries</h2>
            </div>
            <button className="secondary-button" type="button" onClick={() => void loadTickets()}>
              Refresh
            </button>
          </div>

          <div className="queue-layout">
            <div className="ticket-list">
              {tickets.length === 0 ? <p className="empty-state">No tickets yet.</p> : null}
              {tickets.map((ticket) => (
                <button
                  key={ticket.id}
                  type="button"
                  className={`ticket-card ${selectedTicket?.id === ticket.id ? "active" : ""}`}
                  onClick={() => setSelectedTicket(ticket)}
                >
                  <div className="ticket-card-top">
                    <span className="ticket-id">#{ticket.id}</span>
                    <span className={`pill priority-${ticket.priority.toLowerCase()}`}>
                      {ticket.priority}
                    </span>
                  </div>
                  <strong>{ticket.subject}</strong>
                  <p>{ticket.customerEmail}</p>
                  <div className="ticket-meta">
                    <span className="pill neutral">{ticket.status}</span>
                    <span className="pill accent">{ticket.aiCategory}</span>
                  </div>
                </button>
              ))}
            </div>

            <div className="ticket-detail">
              {selectedTicket ? (
                <>
                  <div className="detail-header">
                    <div>
                      <p className="panel-kicker">Selected Ticket</p>
                      <h3>{selectedTicket.subject}</h3>
                    </div>
                    <span className="ticket-id">#{selectedTicket.id}</span>
                  </div>

                  <dl className="detail-grid">
                    <div>
                      <dt>Email</dt>
                      <dd>{selectedTicket.customerEmail}</dd>
                    </div>
                    <div>
                      <dt>Priority</dt>
                      <dd>{selectedTicket.priority}</dd>
                    </div>
                    <div>
                      <dt>Status</dt>
                      <dd>{selectedTicket.status}</dd>
                    </div>
                    <div>
                      <dt>AI Category</dt>
                      <dd>{selectedTicket.aiCategory}</dd>
                    </div>
                  </dl>

                  <div className="detail-section">
                    <h4>Customer message</h4>
                    <p>{selectedTicket.content}</p>
                  </div>

                  <div className="detail-section">
                    <h4>AI Summary</h4>
                    <p>{selectedTicket.aiSummary}</p>
                  </div>

                  <div className="detail-section">
                    <h4>Draft Reply</h4>
                    <p>{selectedTicket.aiDraftReply}</p>
                  </div>
                </>
              ) : (
                <p className="empty-state">Select a ticket to inspect AI output.</p>
              )}
            </div>
          </div>
        </section>
      </main>
    </div>
  );
}

export default App;
