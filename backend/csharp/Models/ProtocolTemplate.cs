namespace Models
{
    public class ProtocolTemplate
    {
        public long Id { get; set; }

        public required string Name { get; set; }

        public string? Description { get; set; }

        public required string Template { get; set; }

        public DateTime CreatedOrEdited { get; set; }

        public long organizationId { get; set; }

        public required Organization Organization { get; set; } 

    }
}
